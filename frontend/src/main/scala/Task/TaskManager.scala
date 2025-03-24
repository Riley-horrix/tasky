package Task

import com.raquo.laminar.api.L.*


/**
  * Enum to represent days of the week.
  */
enum Day(val str: String) {
    case Monday extends Day("Monday");
    case Tuesday extends Day("Tuesday");
    case Wednesday extends Day("Wednesday");
    case Thursday extends Day("Thursday");
    case Friday extends Day("Friday");
    case Saturday extends Day("Saturday");
    case Sunday extends Day("Sunday");
}

/**
  * Container for a specific time in the day.
  * 
  * @param hour 24 hour number.
  * @param minute Minute number.
  */
case class Time(hour: Int, minute: Int) {
    def +(duration: Int) = Time(hour + duration / 60, (minute + duration) % 60)

    override def toString(): String = f"$hour%02d:$minute%02d"
}

object Time {
    def minutesBetween(time: Time, other: Time): Int = ((time.hour * 60 + time.minute) - (other.hour * 60 + other.minute)).abs
}

/**
  * Represents a generic task.
  * 
  * @member id Unique integer identifier.
  * @member title Title for the task.
  * @member description Task description.
  */
sealed trait TaskObject {
    val id: Int;
    val title: String;
    val description: String;
    val expanded: Var[Boolean] = Var(false);
} 

/**
  * Represents a static task, i.e. one with a set time and date it must
  * occur.
  *
  * @param id Unique integer identifier.
  * @param title Title for the task.
  * @param description Task description.
  * @param day Day the task occurs on.
  * @param time The time of day the task should start.
  * @param duration The duration in minutes of the task.
  * @param repeating If the task should repeat weekly.
  */
case class StaticTask(
    val id: Int, 
    val title: String, 
    val description: String, 
    val day: Day, 
    val time: Time, 
    val duration: Int, 
    val repeating: Boolean
) extends TaskObject

/**
  * Represents a dynamic task, i.e. one that the scheduler is free to 
  * relocate into any free space in the calendar.
  *
  * @param id Unique integer identifier.
  * @param title Title for the task.
  * @param description Task description.
  * @param priority Priority of the task, used by the scheduler.
  */
case class DynamicTask(
    val id: Int, 
    val title: String, 
    val description: String, 
    val priority: Int
) extends TaskObject

object TaskManager {
    private val exampleStatic: List[StaticTask] = List(
        StaticTask(0, "Do dishes", "Empty dishwasher, then dry and put away all dishes", Day.Monday, Time(9, 30), 90, true),

        StaticTask(1, "Play games", "Go on the xbox, boot it up and play games", Day.Monday, Time(12, 45), 10, true),

        StaticTask(2, "Do homework", "Remember to finish off maths homework and do other chores", Day.Tuesday, Time(11, 30), 60, true),
    )

    private val exampleDynamic: List[DynamicTask] = List(
        DynamicTask(100, "Work on OS", "Finish file system", 1),
        DynamicTask(101, "Work on DILL", "Create v2.0", 2),
        DynamicTask(102, "Work on Airfix", "Build tanks!", 3),
    )

    private val exampleTasks: List[StaticTask] = exampleStatic ++ exampleDynamic.zipWithIndex.map((task, ind) => StaticTask(task.id, task.title, task. description, Day.fromOrdinal(ind), Time(17, 0), 90, false))

    // List of all of the current static user tasks
    private val staticTaskList: Var[List[StaticTask]] = Var(exampleStatic ++ exampleStatic.map(task => StaticTask(task.id + 5, task.title, task.description, task.day, task.time, task.duration, task.repeating)))

    // List of all of the current dynamic user tasks
    private val dynamicTaskList: Var[List[DynamicTask]] = Var(exampleDynamic)

    // List of all of the dynamic user tasks mapped to static tasks, appended to existing static tasks
    private val mappedTaskList: Var[List[StaticTask]] = Var(exampleTasks)

    // List of all of the current static user tasks
    val getStaticTaskList: Signal[List[StaticTask]] = staticTaskList.signal

    // List of all of the current dynamic user tasks
    val getDynamicTaskList: Signal[List[DynamicTask]] = dynamicTaskList.signal

    // List of all of the dynamic user tasks mapped to static tasks, appended to existing static tasks
    val getMappedTaskList: Signal[List[StaticTask]] = mappedTaskList.signal
}
