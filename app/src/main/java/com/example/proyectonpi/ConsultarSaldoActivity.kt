package com.example.proyectonpi

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlin.random.Random
import kotlin.math.round
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonpi.databinding.ActivityConsultarSaldoBinding

class ConsultarSaldoActivity : BaseActivity() {

    private lateinit var tvNfcStatus: TextView
    private lateinit var tvNfcSaldo: TextView
    private lateinit var tvMensajeSaldo: TextView
    private lateinit var btnConsultarOtra: Button
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var binding: ActivityConsultarSaldoBinding
    private var nfcEnabled: Boolean = true // Variable para controlar la lectura de tarjetas

    private val nfcStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
                val state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF)
                if (state==NfcAdapter.STATE_OFF){
                    resetUI()
                    updateNfcStatus("NFC está desactivado")
                }
                else if (state==NfcAdapter.STATE_ON){
                    updateNfcStatus("Acerque su TUI a la parte trasera de su móvil")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsultarSaldoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar elementos de la interfaz
        tvNfcStatus = findViewById(R.id.tvNfcStatus)
        tvNfcSaldo = findViewById(R.id.tvNfcSaldo)
        tvMensajeSaldo = findViewById(R.id.tvMensajeSaldo)
        btnConsultarOtra = findViewById(R.id.btnConsultarOtra)

        // Ocultar el botón al inicio
        btnConsultarOtra.visibility = View.GONE

        // Configurar acción del botón
        btnConsultarOtra.setOnClickListener {
            resetUI()
        }

        // Obtener el adaptador NFC del dispositivo
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Verificar si el dispositivo soporta NFC y si está habilitado
        if (nfcAdapter == null) {
            tvNfcStatus.text = "El dispositivo no soporta NFC"
        } else if (!nfcAdapter.isEnabled) {
            tvNfcStatus.text = "NFC está desactivado"
        } else {
            tvNfcStatus.text = "Acerque su TUI a la parte trasera de su móvil"
        }
    }

    override fun onResume() {
        super.onResume()
        // Activar Foreground Dispatch solo si NFC está habilitado
        if (nfcEnabled) {
            val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
        }

        // Registrar el receptor para escuchar los cambios de estado del NFC
        val filter = IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
        registerReceiver(nfcStateReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        // Desactivar Foreground Dispatch cuando la actividad se pausa
        nfcAdapter.disableForegroundDispatch(this)

        // Desregistrar el receptor del cambio de estado del NFC
        unregisterReceiver(nfcStateReceiver)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (!nfcEnabled) return // Evita nuevas lecturas si ya se ha leído una tarjeta

        // Obtener el tag NFC detectado
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            val mifareClassic = MifareClassic.get(tag)
            if (mifareClassic != null) {
                try {
                    mifareClassic.connect()

                    // Verificar si la tarjeta es MiFare Classic 4K
                    if (mifareClassic.type == MifareClassic.TYPE_CLASSIC && mifareClassic.sectorCount == 40) {
                        val saldo = Random.nextDouble(0.0, 20.0)
                        val saldoRedondeado = round(saldo * 100) / 100

                        // Mostrar saldo y mensaje
                        tvMensajeSaldo.text = "Su saldo es:"
                        tvNfcSaldo.text = "$saldoRedondeado €"
                        tvNfcStatus.text = "Tarjeta leída correctamente"

                        // Mostrar botón y bloquear nuevas lecturas
                        btnConsultarOtra.visibility = View.VISIBLE
                        nfcEnabled = false

                    } else {
                        tvNfcStatus.text = "Tarjeta no válida"
                    }
                } catch (e: Exception) {
                    tvNfcStatus.text = "Error al leer la tarjeta: ${e.message}"
                } finally {
                    mifareClassic.close()
                }
            }
        }
    }

    private fun updateNfcStatus(message: String) {
        tvNfcStatus.text = message
    }

    private fun resetUI() {
        // Reiniciar la interfaz
        tvNfcStatus.text = "Acerque su TUI a la parte trasera de su móvil"
        tvMensajeSaldo.text = ""
        tvNfcSaldo.text = ""

        // Ocultar botón y reactivar lectura NFC
        btnConsultarOtra.visibility = View.GONE
        nfcEnabled = true
        onResume() // Reanudar Foreground Dispatch
    }
}
