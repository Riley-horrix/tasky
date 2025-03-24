import com.raquo.laminar.api.L.*

object MainView {
    def apply(): Element = div(
        State.isLightMode --> State.updateDomTheme(),
        
        child <-- MainViewController.getCurrentViewElement
    )
}
