package pl.coderslab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class TaskManager {
    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = loadDataToTab(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        printOptions(OPTIONS);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Zase příště." + ConsoleColors.RESET);
                    System.exit(0);
                    break;
                case "add":
                    addTask(scanner);
                    break;
                case "remove":
                    int numberToRemove = getTheNumber(scanner);
                    removeTask(tasks, numberToRemove);
                    System.out.println("Úkol byl odebrán.");
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Vyberte správnou možnost.");
            }
            printOptions(OPTIONS);
        }
        scanner.close();
    }

    public static void printOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE + "Vyberte možnost:" + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    public static String[][] loadDataToTab(String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("Seznam je prázdný");
            return new String[0][0];
        }
        String[][] tab = null;
        try {
            List<String> lines = Files.readAllLines(path);
            if (lines.isEmpty()) {
                return new String[0][0];
            }
            tab = new String[lines.size()][lines.get(0).split(",").length];
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void printTab(String[][] tab) {
        if (tab.length == 0) {
            System.out.println("Žádné úkoly k zobrazení.");
            return;
        }
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.println("Zadejte popis úkolu:");
        String description = scanner.nextLine();
        System.out.println("Zadejte datum:");
        String dueDate = scanner.nextLine();
        System.out.println("Je Váš ukol důležitý?: true/false");
        String isImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = isImportant;
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getTheNumber(Scanner scanner) {
        System.out.println("Vyberte číslo úkolu který chcete smazat.");
        String n = scanner.nextLine();

        while (!isNumberGreaterEqualZero(n) || Integer.parseInt(n) >= tasks.length) {
            System.out.println("Zadaná špatná hodnota. Zadejte hodnotu mezi 0 a " + (tasks.length - 1));
            n = scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element neexistuje.");
        }
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path path = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
