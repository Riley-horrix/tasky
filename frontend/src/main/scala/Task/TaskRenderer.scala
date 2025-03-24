package Task

import com.raquo.laminar.api.L.*
import com.raquo.laminar.api.features.unitArrows

object TaskRenderer {
    private def renderTimeFrame(time: Time, duration: Int) = s"$time - ${time + duration}"

    def renderStatic(id: Int, task: StaticTask, signal: Signal[StaticTask]): Element = div(
        className := "task-task-style",
        text <-- signal.map(_.title),
        p(
            className := "task-task-attr",
            text <-- signal.map(_.day.str)
        ),
        p(
            className := "task-task-attr",
            text <-- signal.map(task => renderTimeFrame(task.time, task.duration))
        ),
        p(
            className := "task-task-attr task-task-desc",
            text <-- signal.map(_.description),
            cls("expanded") <-- signal.flatMapSwitch(task => task.expanded.signal)
        ),
        onClick --> task.expanded.invert()
    )

    def renderDynamic(id: Int, task: DynamicTask, signal: Signal[DynamicTask]): Element = div(
        className := "task-task-style",
        text <-- signal.map(_.title),
        p(
            className := "task-task-attr",
            text <-- signal.map(task => s"Priority: ${task.priority}")
        ),
        p(
            className := "task-task-attr task-task-desc",
            text <-- signal.map(_.description),
            cls("expanded") <-- signal.flatMapSwitch(t => t.expanded.signal)
        ),
        onClick --> task.expanded.invert()
    )

    def renderCalendar(task: StaticTask): Element = div(
        gridRow := 2,
        "h1"
    )
}
