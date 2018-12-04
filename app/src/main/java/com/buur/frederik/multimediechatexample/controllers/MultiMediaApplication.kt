package com.buur.frederik.multimediechatexample.controllers

import android.app.Application
import com.buur.frederik.multimediechatexample.BuildConfig
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


class MultiMediaApplication: Application() {

    var socket: Socket? = null

    override fun onCreate() {
        super.onCreate()

        instance = this

        setupFabric()
        setupLeakCanary()
        setupSocketIOConnection()
        setupRealm()

    }

    private fun setupFabric() {
        Fabric.with(this, Crashlytics())
    }

    private fun setupLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
        }
    }

    private fun setupSocketIOConnection() {
        try {
            /*
            val mySSLContext = SSLContext.getInstance("SSL")
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun checkClientTrusted(p0: Array<out java.security.cert.X509Certificate>?, p1: String?) {
                }

                override fun checkServerTrusted(p0: Array<out java.security.cert.X509Certificate>?, p1: String?) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            mySSLContext.init(null, trustAllCerts, null)

            val myHostnameVerifier = HostnameVerifier { hostname, session -> true }

            val opts = IO.Options()
            opts.sslContext = mySSLContext
            opts.hostnameVerifier = myHostnameVerifier
            */

            socket = IO.socket(ServiceGenerator.MM_BASE_URL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    private fun setupRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }

    fun setConnectionListener(listener: ConnectionHandler.ConnectionListener) {
        ConnectionHandler.connectionListener = listener
    }

    companion object {
        var instance: MultiMediaApplication? = null
    }

}