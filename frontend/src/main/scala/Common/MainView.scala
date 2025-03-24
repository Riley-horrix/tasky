import com.raquo.laminar.api.L.*

object MainView {
    def apply(): Element = div(
        className := "main-view",

        State.isLightMode --> State.updateDomTheme(),
        
        child <-- MainViewController.getCurrentViewElement
    )
}
