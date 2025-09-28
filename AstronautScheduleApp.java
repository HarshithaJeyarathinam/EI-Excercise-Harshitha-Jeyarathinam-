import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// ==========================
// Task Entity
// ==========================
class Task {
    private String description;
    private String startTime;
    private String endTime;
    private String priority;
    private boolean completed;

    public Task(String description, String startTime, String endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    public String getDescription() { return description; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setDescription(String description) { this.description = description; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setPriority(String priority) { this.priority = priority; }
    public void markCompleted() { this.completed = true; }

    @Override
    public String toString() {
        return startTime + " - " + endTime + ": " + description +
                " [" + priority + "]" + (completed ? " [Completed]" : "");
    }

    public String toFileFormat() {
        return description + "|" + startTime + "|" + endTime + "|" + priority + "|" + completed;
    }

    public static Task fromFileFormat(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 5) return null;
        Task t = new Task(parts[0], parts[1], parts[2], parts[3]);
        if (Boolean.parseBoolean(parts[4])) {
            t.markCompleted();
        }
        return t;
    }
}

// ==========================
// Factory Pattern
// ==========================
class TaskFactory {
    public Task createTask(String description, String startTime, String endTime, String priority) {
        return new Task(description, startTime, endTime, priority);
    }
}

// ==========================
// Observer Pattern
// ==========================
interface TaskObserver {
    void onTaskConflict(Task newTask, Task existingTask);
}

class ConflictNotifier implements TaskObserver {
    @Override
    public void onTaskConflict(Task newTask, Task existingTask) {
        System.out.println("Conflict: Task '" + newTask.getDescription() +
                "' overlaps with '" + existingTask.getDescription() + "'");
        Logger.log("Conflict detected between: " + newTask.getDescription() +
                " and " + existingTask.getDescription());
    }
}

// ==========================
// Logger Utility
// ==========================
class Logger {
    private static final String LOG_FILE = "schedule_log.txt";

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String fullMessage = "[" + timestamp + "] " + message;

        // Print to console
        System.out.println("Log: " + fullMessage);

        // Append to file
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(fullMessage + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write log.");
        }
    }
}

// ==========================
// Singleton Pattern
// ==========================
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private List<TaskObserver> observers;
    private static final String FILE_NAME = "tasks.txt";

    private ScheduleManager() {
        tasks = new ArrayList<>();
        observers = new ArrayList<>();
        loadTasksFromFile();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    private void notifyConflict(Task newTask, Task existingTask) {
        for (TaskObserver observer : observers) {
            observer.onTaskConflict(newTask, existingTask);
        }
    }

    // Add Task
    public void addTask(Task task) {
        if (!isValidTime(task.getStartTime()) || !isValidTime(task.getEndTime())) {
            System.out.println("Error: Invalid time format (HH:MM).");
            Logger.log("Invalid time format for task: " + task.getDescription());
            return;
        }
        for (Task t : tasks) {
            if (isOverlap(task, t)) {
                notifyConflict(task, t);
                return;
            }
        }
        tasks.add(task);
        saveTasksToFile();
        System.out.println("Task added successfully.");
        Logger.log("Task added: " + task.getDescription());
    }

    // Remove Task
    public void removeTask(String description) {
        Task toRemove = findTask(description);
        if (toRemove != null) {
            tasks.remove(toRemove);
            saveTasksToFile();
            System.out.println("Task removed successfully.");
            Logger.log("Task removed: " + description);
        } else {
            System.out.println("Error: Task not found.");
        }
    }

    // Edit Task
    public void editTask(String description, String newDesc, String newStart, String newEnd, String newPriority) {
        Task t = findTask(description);
        if (t != null) {
            t.setDescription(newDesc);
            t.setStartTime(newStart);
            t.setEndTime(newEnd);
            t.setPriority(newPriority);
            saveTasksToFile();
            System.out.println("Task updated successfully.");
            Logger.log("Task edited: " + description + " → " + newDesc);
        } else {
            System.out.println("Error: Task not found.");
        }
    }

    // View All Tasks
    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
            return;
        }
        tasks.sort(Comparator.comparing(Task::getStartTime));
        for (Task t : tasks) {
            System.out.println(t);
        }
    }

    // Filter by Priority
    public void filterByPriority(String priority) {
        boolean found = false;
        for (Task t : tasks) {
            if (t.getPriority().equalsIgnoreCase(priority)) {
                System.out.println(t);
                found = true;
            }
        }
        if (!found) System.out.println("No tasks with priority: " + priority);
    }

    // Mark Completed
    public void markTaskCompleted(String description) {
        Task t = findTask(description);
        if (t != null) {
            t.markCompleted();
            saveTasksToFile();
            System.out.println("Task marked as completed.");
            Logger.log("Task completed: " + description);
        } else {
            System.out.println("Error: Task not found.");
        }
    }

    // Helpers
    private Task findTask(String description) {
        for (Task t : tasks) {
            if (t.getDescription().equalsIgnoreCase(description)) return t;
        }
        return null;
    }

    private boolean isOverlap(Task t1, Task t2) {
        return (t1.getStartTime().compareTo(t2.getEndTime()) < 0 &&
                t1.getEndTime().compareTo(t2.getStartTime()) > 0);
    }

    private boolean isValidTime(String time) {
        return time.matches("([01]\\d|2[0-3]):[0-5]\\d");
    }

    // Persistence
    private void saveTasksToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                pw.println(t.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
        }
    }

    private void loadTasksFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                Task t = Task.fromFileFormat(sc.nextLine());
                if (t != null) tasks.add(t);
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks.");
        }
    }
}

// ==========================
// Console Application
// ==========================
public class AstronautScheduleApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ScheduleManager manager = ScheduleManager.getInstance();
        TaskFactory factory = new TaskFactory();
        manager.addObserver(new ConflictNotifier());

        while (true) {
            System.out.println("\n==== Astronaut Daily Schedule ====");
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View All Tasks");
            System.out.println("4. Mark Task Completed");
            System.out.println("5. Edit Task");
            System.out.println("6. Filter by Priority");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter description: ");
                    String desc = sc.nextLine();
                    System.out.print("Enter start time (HH:MM): ");
                    String start = sc.nextLine();
                    System.out.print("Enter end time (HH:MM): ");
                    String end = sc.nextLine();
                    System.out.print("Enter priority (High/Medium/Low): ");
                    String priority = sc.nextLine();
                    Task newTask = factory.createTask(desc, start, end, priority);
                    manager.addTask(newTask);
                    break;

                case 2:
                    System.out.print("Enter task description to remove: ");
                    String removeDesc = sc.nextLine();
                    manager.removeTask(removeDesc);
                    break;

                case 3:
                    manager.viewTasks();
                    break;

                case 4:
                    System.out.print("Enter task description to mark completed: ");
                    String compDesc = sc.nextLine();
                    manager.markTaskCompleted(compDesc);
                    break;

                case 5:
                    System.out.print("Enter task description to edit: ");
                    String editDesc = sc.nextLine();
                    System.out.print("New description: ");
                    String newDesc = sc.nextLine();
                    System.out.print("New start time (HH:MM): ");
                    String newStart = sc.nextLine();
                    System.out.print("New end time (HH:MM): ");
                    String newEnd = sc.nextLine();
                    System.out.print("New priority: ");
                    String newPriority = sc.nextLine();
                    manager.editTask(editDesc, newDesc, newStart, newEnd, newPriority);
                    break;

                case 6:
                    System.out.print("Enter priority to filter (High/Medium/Low): ");
                    String filterPriority = sc.nextLine();
                    manager.filterByPriority(filterPriority);
                    break;

                case 7:
                    System.out.println("Exiting... Have a productive mission!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
