package Task

import com.raquo.laminar.api.L.*

case class PageSelection(name: String, link: String)

object TaskView {
    private lazy val leftBar: Element = sectionTag(
        className := "task-left-bar",
        div(
            className := "task-task-container",
            "Static Tasks",
            div(
                className := "task-task-inner-container",
                children <-- TaskManager.getStaticTaskList.split(
                    key = _.id
                )(
                    project = TaskRenderer.renderStatic
                )
            )
        ),
        div(
            className := "task-task-container",
            "Dynamic Tasks",
            div(
                className := "task-task-inner-container",
                children <-- TaskManager.getDynamicTaskList.split(
                    key = _.id
                )(
                    project = TaskRenderer.renderDynamic
                )
            )
        )
    )

    private lazy val pages: List[PageSelection] = List(
        PageSelection("Calendar", "/calendar"),
        PageSelection("Projects", "/projects"),
    )

    private lazy val taskPageSelector: Element = div(
        className := "task-page-selector",

        pages.map(page => h4(
            page.name,
            className := "task-page-selector-element"
        )),
        i(className := "bi bi-person-circle task-page-selector-element")
    )
        
    private lazy val topBar: Element = sectionTag(
        className := "task-top-bar",
        
        h1(
            className := "task-title",
            "Tasky"
        ),
        taskPageSelector,
    )

    def apply(): Element = mainTag(
        className := "task-main",
        leftBar,
        div(
            className := "task-view-container",
            topBar,
            child <-- TaskViewController.getTaskViewElement
        )
    )
}
