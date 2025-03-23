package View

import com.raquo.laminar.api.L.*

object MainView {
    def apply(): Element = div(
        child <-- MainViewController.getCurrentViewElement
    )
}
