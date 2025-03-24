import com.raquo.laminar.api.L.*

import org.scalajs.dom

object State {
    // Reactive variable representing the current page theme.
    private val theme: Var[Boolean] = Var(!dom.window.matchMedia("(prefers-color-scheme: dark)").matches)

    // Get signal indicating whether app is in light or dark mode.
    val isLightMode: Signal[Boolean] = theme.signal

    // Set theme as DOM CSS page attribute
    // This needs to be attached in MainView to be able to switch themes
    def updateDomTheme(): Observer[Boolean] = {
        Observer(
            (isLightMode: Boolean) => dom.document
                .documentElement
                .setAttribute("data-theme", if isLightMode then "dark" else "light")
        )
    }
}
