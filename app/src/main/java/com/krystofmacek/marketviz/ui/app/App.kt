package com.krystofmacek.marketviz.ui.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp kicks off the code generation of the Hilt components and
 *  also generates a base class for your application that uses those generated components.
 * */
@HiltAndroidApp
class App: Application() {
}