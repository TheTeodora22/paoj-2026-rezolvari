package com.pao.laboratory03.bonus.service;

import java.util.*;
import com.pao.laboratory03.bonus.model.*;
import com.pao.laboratory03.bonus.exceptii.*;

public class TaskService {
    private static TaskService instance;
    private static int id = 0;
    private TaskService() {

    }
    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }
    Map<String, Task> tasksById = new HashMap<>();
    Map<Priority, List<Task>> tasksByPriority = new HashMap<>();    
    List<String> auditLog = new ArrayList<>();

    public void addTask(String title, Priority priority) {
        ++id;
        String taskId = "T";
        if(id<10)
        {
            taskId += "00" + id;
        }
        else if(id<100)
        {
            taskId += "0" + id;
        }
        else
        {
            taskId += id;
        }
        Task task = new Task(taskId, title,Status.TODO,priority, null);
        tasksById.put(taskId, task);
        tasksByPriority.put(priority,
        tasksByPriority.getOrDefault(priority, new ArrayList<>()));
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + taskId + ": '" + title + "' (" + priority + ")");
        System.out.println("Adaugat: " + task);
    }
    public Task addTaskWithId(String taskId, String title, Priority priority) {
        if (tasksById.containsKey(taskId)) {
            throw new DuplicateTaskException("Task cu ID " + taskId + " exista deja.");
        }

        Task task = new Task(taskId, title, Status.TODO, priority, null);

        tasksById.put(taskId, task);
        tasksByPriority.put(priority,
        tasksByPriority.getOrDefault(priority, new ArrayList<>()));
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + taskId + ": '" + title + "' (" + priority + ")");

        return task;
    }
    public void assignTask(String taskId, String assignee) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task cu ID " + taskId + " nu exista");
        }
        task = new Task(task.getId(), task.getTitle(), task.getStatus(), task.getPriority(), assignee);
        tasksById.put(taskId, task);
        auditLog.add("[ASSIGN] " + taskId + ": -> " + assignee);
        System.out.println(taskId + ": -> " + assignee);
    }
    public void changeStatus(String taskId, Status newStatus) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task cu ID " + taskId + " nu exista");
        }
        if (!task.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(task.getStatus(), newStatus);
        }
        auditLog.add("[STATUS] " + taskId + ": " + task.getStatus() + " -> " + newStatus);
        System.out.println(taskId + ": " + task.getStatus() + " -> " + newStatus);
        task = new Task(task.getId(), task.getTitle(), newStatus, task.getPriority(), task.getAssignee());
        tasksById.put(taskId, task);
        
    }
    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }
    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();
        for (Task task : tasksById.values()) {
            summary.put(task.getStatus(), summary.getOrDefault(task.getStatus(), 0L) + 1);
        }
        return summary;
    }
    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                unassigned.add(task);
            }
        }
        return unassigned;
    }
    public void printAuditLog() {
        for (String log : auditLog) {
            System.out.println(log);
        }
    }
    public double getTotalUrgencyScore(int baseDays)
    {
        double totalScore = 0.0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                totalScore += task.getPriority().calculateScore(baseDays);
            }
        }
        return totalScore;
    }
}
