import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class sort {
    public static void main(String[] args) {
        try {
            HashMap<String, String> options = new HashMap<>();
            ArrayList<String> paths = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        options.put(args[i], args[i + 1]);
                        i++;
                    } else {
                        options.put(args[i], null);
                    }
                }
            }
            Collections.addAll(paths, args);
            Path path, path_save;
            ArrayList<Long> nums = new ArrayList<>();
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<Double> doubles = new ArrayList<>();
            int stats = 0;
            int f_append = 0;
            String option_name;
            StringBuilder name_i = new StringBuilder("integers.txt");
            StringBuilder name_f = new StringBuilder("floats.txt");
            StringBuilder name_s = new StringBuilder("strings.txt");
            String sys_path_str = System.getProperty("user.dir");
            Path sys_path = Paths.get(sys_path_str);

            try {
                for (String option : options.keySet()) {
                    switch (option) {
                        case "-p":
                            String text0 = options.get("-p");
                            if (text0 != null) {
                                option_name = text0;
                                name_i.insert(0, option_name);
                                name_f.insert(0, option_name);
                                name_s.insert(0, option_name);
                            } else {
                                System.out.println("""
                                        ***
                                        Внимание! Префикс после команды -p не указан или указан с ошибкой.\s
                                        Будут использованы стандартные наименования файлов для результатов.

                                        ***""");
                            }
                            break;
                        case "-s":
                            stats = 1;
                            break;
                        case "-f":
                            stats = 2;
                            break;
                        case "-a":
                            f_append = 1;
                            break;
                        case "-o":
                            String text3 = options.get("-o");
                            if (text3 != null) {
                                String user_path;
                                if (text3.startsWith("/")) {
                                    user_path = text3.substring(1);
                                } else user_path = text3;
                                path_save = Paths.get(user_path);
                                if (Files.exists(path_save.toAbsolutePath())) {
                                    sys_path = path_save;
                                } else System.out.println("""
                                        ***
                                        Внимание! Директория сохранения файлов для опции -o не существует.\s
                                        Результаты будут сохранены в директории расположения программы.

                                        ***""");
                            } else {
                                System.out.println("""
                                        ***
                                        Внимание! Директория сохранения файлов команды -o не указана или указана с ошибкой.\s
                                        Результаты будут сохранены в директории расположения программы.

                                        ***""");
                            }
                            break;
                        default:
                            System.out.println("Внимание! Несуществующая опция: " + option+" была проигнорирована.");

                    }
                }
                int ii = 0;
                for (String paths_i : paths) {
                    path = Paths.get(paths_i);
                    Scanner scan;
                    try {
                        scan = new Scanner(path);
                    } catch (IOException e) {
                        if (!path.startsWith("-") && !options.containsValue(path.toString()) && !options.containsKey(path.toString())) {
                            while (ii < 1) {
                                create_readme();
                                System.out.println("Внимание! Не найден один или несколько исходных файлов." +
                                        "\nПожалуйста, ознакомьтесь с инструкцией в файле readme.txt");
                                ii++;
                            }
                        }
                        continue;
                    }
                    while (scan.hasNext()) {
                        if (scan.hasNextLong()) {
                            long i = scan.nextLong();
                            nums.add(i);

                        } else if (scan.hasNextDouble()) {
                            double d = scan.nextDouble();
                            doubles.add(d);

                        } else {
                            String str = scan.nextLine();
                            if (!str.isEmpty()) {
                                try {
                                    double d = Double.parseDouble(str);
                                    doubles.add(d);
                                } catch (NumberFormatException e) {
                                    lines.add(str);
                                }

                            }

                        }
                    }


                }
            } catch (Exception e) {
                create_readme();
//                e.printStackTrace();
                System.out.println("Внимание! Произошла ошибка, пожалуйста, ознакомьтесь с инструкцией в файле readme.txt");
            }
            // Переменные для статистики
            int stat_int = nums.size(),
                    stat_floats = doubles.size(),
                    stat_string = lines.size(),
                    min_string = find_min_max_str("MIN", lines),
                    max_string = find_min_max_str("MAX", lines);
            long min_int = !nums.isEmpty()?Collections.min(nums):0,
                    max_int = !nums.isEmpty()?Collections.max(nums):0,
                    sum_int = sum_array_int(nums),
                    average_int = !nums.isEmpty()?(sum_int / nums.size()):0;
            double min_float = !doubles.isEmpty()?Collections.min(doubles):0.0,
                    max_float = !doubles.isEmpty()?Collections.max(doubles):0.0,
                    sum_float = sum_array_double(doubles),
                    average_float = !doubles.isEmpty()?(sum_float / doubles.size()):0.0;


            if (stats == 1) {
                //Краткая статистика
                System.out.println("\nСтатистика выполнения программы: \n\n" +
                        "Были отсортированы следующие элементы:\n" +
                        "Строки - " + stat_string + "\n" +
                        "Целые числа - " + stat_int + "\n" +
                        "Вещественные числа - " + stat_floats);
            } else if (stats == 2) {
                //Полная статистика
                System.out.println("\nСтатистика выполнения программы: \n\n" +
                        "Были отсортированы следующие элементы:\n" +
                        "*****\n" +
                        "Строки - " + stat_string + "\n" +
                        "Самая короткая строка - " + min_string + "\n" +
                        "Самая длинная строка - " + max_string +
                        "\n*****\n" +
                        "Целые числа - " + stat_int + "\n" +
                        "Минимальное значение - " + min_int + "\n" +
                        "Максимальное значение - " + max_int + "\n" +
                        "Сумма всех значений - " + sum_int + "\n" +
                        "Ср.арифметическое - " + average_int +
                        "\n*****\n" +
                        "Вещественные числа - " + stat_floats + "\n" +
                        "Минимальное значение - " + min_float + "\n" +
                        "Максимальное значение - " + max_float + "\n" +
                        "Сумма всех значений - " + sum_float + "\n" +
                        "Ср.арифметическое - " + average_float +
                        "\n\n*****\n");
            }
            //Работа с файлами
            Path file_i = Path.of(sys_path+"\\"+ name_i),
                    file_s = Path.of(sys_path+"\\"+ name_s),
                    file_f = Path.of(sys_path+"\\"+ name_f);
            if (f_append == 1){
                if (Files.exists(file_i)){
                    try {
                        if (!nums.isEmpty()) {
                            for (long i : nums) {
                                String int_s = Long.toString(i);
                                Files.write(file_i, int_s.getBytes(), StandardOpenOption.APPEND);
                                Files.write(file_i, "\n".getBytes(), StandardOpenOption.APPEND);
                            }
                        }
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                }else{
                    if (!nums.isEmpty()) {
                        Files.createFile(file_i);
                        for (long i : nums) {
                            String int_s = Long.toString(i);
                            Files.write(file_i, int_s.getBytes(), StandardOpenOption.APPEND);
                            Files.write(file_i, "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                }
                if (Files.exists(file_s)){
                    try {
                        if (!lines.isEmpty()) {
                            for (String s : lines) {
                                Files.write(file_s, s.getBytes(), StandardOpenOption.APPEND);
                                Files.write(file_s, "\n".getBytes(), StandardOpenOption.APPEND);
                            }
                        }
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                }else{
                    if (!lines.isEmpty()) {
                        Files.createFile(file_s);
                        for (String s : lines) {
                            Files.write(file_s, s.getBytes(), StandardOpenOption.APPEND);
                            Files.write(file_s, "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                }
                if (Files.exists(file_f)) {
                    try {
                        if (!doubles.isEmpty()) {
                            for (Double f : doubles) {
                                String double_s = Double.toString(f);
                                Files.write(file_f, double_s.getBytes(), StandardOpenOption.APPEND);
                                Files.write(file_f, "\n".getBytes(), StandardOpenOption.APPEND);
                            }
                        }
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                }else{
                    if (!doubles.isEmpty()) {
                        Files.createFile(file_f);
                        for (Double f : doubles) {
                            String double_s = Double.toString(f);
                            Files.write(file_f, double_s.getBytes(), StandardOpenOption.APPEND);
                            Files.write(file_f, "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                }
            }
            else {
                try {
                    if (!nums.isEmpty()) {
                        try {
                            Files.delete(file_i);
                        }catch (IOException ignored){}
                        Files.createFile(file_i);
                        for (long i : nums) {
                            String int_s = Long.toString(i);
                            Files.write(file_i, int_s.getBytes(), StandardOpenOption.APPEND);
                            Files.write(file_i, "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                }
                try {
                    if (!lines.isEmpty()) {
                        try {
                            Files.delete(file_s);
                        }catch (IOException ignored){}
                        Files.createFile(file_s);
                        for (String s : lines) {
                            Files.write(file_s, s.getBytes(), StandardOpenOption.APPEND);
                            Files.write(file_s, "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                }
                try {
                    if (!doubles.isEmpty()) {
                        try {
                            Files.delete(file_f);
                        }catch (IOException ignored){}
                        Files.createFile(file_f);
                        for (double f : doubles) {
                            String double_s = Double.toString(f);
                            Files.write(file_f, double_s.getBytes(), StandardOpenOption.APPEND);
                            Files.write(file_f, "\n".getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            create_readme();
        }

    }
        public static int find_min_max_str(String min_max, ArrayList<String> list) {
            int result = 0;
            if (min_max.equals("MIN")){
                if (list.isEmpty())
                    return result;
                String min_str = list.getFirst();
                for (String s : list) {
                    if (s.length() < min_str.length())
                        min_str = s;
                }
                result = min_str.length();
            }else if (min_max.equals("MAX")){
                if (list.isEmpty())
                    return result;
                String max_str = list.getFirst();
                for (String s : list) {
                    if (s.length() > max_str.length())
                        max_str = s;
                }
                result = max_str.length();
            }
        return result;
        }
        public static long sum_array_int(ArrayList<Long> list) {
        long result = 0;
            if (list.isEmpty())
                    return result;

            for (long i : list)
                result = result+i;
        return result;
        }
        public static double sum_array_double(ArrayList<Double> list) {
        double result = 0.0E00;
        if (list.isEmpty())
            return result;

        for (double i : list)
            result = result+i;
        return result;
    }
        public static void create_readme(){
            Path file_readme = Path.of("readme.txt");
            String text_readme = """

                    ***
                    java version "21.0.2" 2024-01-16 LTS
                    Java(TM) SE Runtime Environment (build 21.0.2+13-LTS-58)
                    Java HotSpot(TM) 64-Bit Server VM (build 21.0.2+13-LTS-58, mixed mode, sharing)
                    ***

                    Программа sort.jar предназначена для сортировки данных, получаемых из входных *.txt файлов,
                    в соответствии с типом этих данных.
                    
                    Особенности реализации:
                    - При возниконовении ошибки (не корректный входной файл или отсутствуют входные файлы), программа
                    проверяет наличие настоящего файла readme.txt, создает его, в случае отсутствия и выводит сообщение
                    с просьбой ознакомиться с инструкцией.
                    - При вводе несуществующих опций запуска, или некорректных аргументах опций -p и -o программа выводит
                    сообщение об ошибке, но продолжает работу.
                    - При использовании опции -a, когда выходные файлы отсутствуют, при наличии отфильтрованных данных
                    файл соответствующего типа будет создан.

                    Для запуска программы без дополнительных обработок, необходимо ввести в командную строку команду:
                    -java -jar sort.jar <Имя файла>  (Пример запуска: -java -jar sort.jar in1.txt)
                    Примечание: Программа имеет возможность обрабатывать данные из нескольких передваемых файлов
                    (Пример запуска:  -java -jar sort.jar in1.txt in2.txt in3.txt)*
                    *-Если при запуске программы не будут указаны файлы для обработки (один или более) - программа не будет выполнена.

                    По умолчанию программа сохраняет результаты выполнения в текущей директории под стандартными именами,
                    в соответствии с типом. (Пример наименований: integers.txt, strings.txt, floats.txt)

                    --------------------------------------------
                    Дополнительные опции для запуска программы:

                    При необходимости программу возможно запустить с применением дополнительных опций.
                    (Пример запуска с использованием опций: -java -jar sort.jar in1.txt -s -p result_ -o Home/dir)*
                    *-Запуск программы с применением указанных аргументов выведет краткую статистику выполнения программы,
                    добавит префикс "result_" к именам выходных файлов и сохранит результаты в директории Home/dir, если она существует.

                    ____________________________________________
                    Подробное описание доступных опций:

                    -p <result> (Пример запуска:  -java -jar sort.jar in1.txt -p result_) - Добавляет префикс <result> к именам выходных файлов.

                    -o <path> (Пример запуска:   -java -jar sort.jar in1.txt -o Home/dir) - Cохраняет результаты в директории Home/dir, если она существует.

                    -s (Пример запуска:    -java -jar sort.jar in1.txt -s) - Выводит краткую статистику выполнения программы, содержащую кол-во
                    обработанных элементов соответстующего типа данных.

                    -f (Пример запуска:    -java -jar sort.jar in1.txt -f) - Выводит полную статистику выполнения программы, содержащую кол-во
                    обработанных элементов соответстующего типа данных, а также дополнительную информацию, в соответствии с типом:
                    для строк - Максимальную и минимальную длину отсортированных строк.
                    для целочисленных значений и вещественных чисел - Максимальное/Минимальное значение, сумму всех значений, а также ср.арифметическое.

                    -a (Пример запуска:    -java -jar sort.jar in1.txt -a) - Проверяет существование выходных файлов в указанной директории,
                    а также с указанным именем и при их наличии дозаписывает результаты в указанные файлы.*
                    *-Поскольку в данном примере не используются дополнительные опции кроме -a, программа проверит наличие файлов в текущей директории и
                    со стандартными наименованиями. Если выходные файлы отсутствуют, при наличии отфильтрованных данных
                    файл соответствующего типа будет создан.

                    ***""";
            try {
                Files.createFile(file_readme);
                Files.write(file_readme, text_readme.getBytes(), StandardOpenOption.APPEND);
            }catch (Exception t){
//                t.printStackTrace();
            }
        }

    }