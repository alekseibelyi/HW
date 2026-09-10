import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class chetNeChet {
   public static Boolean isEven(int n) {
        return n % 2 ==0;
    }
public  static void printResult(int n) {
    if (isEven(n)){
        System.out.println(n+ " - Чётное число");
    } else {
        System.out.println(n + " - Нечётное число");
    }
}
 public static void main(String[] args) {
        printResult(2);
        printResult(3);
        printResult(5);
        printResult(10);
        printResult(999);
 }
}

class Age {
  public static String checkAccess(int age) {
      if (age > 18) {
          return "Allowed";
      } else {
          return "Denied";
      }
  }
  public static void main(String[] args) {
      printAccessResult(10);
      printAccessResult(17);
      printAccessResult(18);
      printAccessResult(21);
      printAccessResult(99);
  }
public static void printAccessResult(int age) {
    System.out.println("Возраст: " + age + ", доступ: " + checkAccess(age));
}
}

class bolsheMensheNull {
    public static boolean isPositive(int m) {
        return m >= 0 ? true : false;
    }

    public static void main(String[] args) {
        int number = -4;

        System.out.println("Число: " + number + " " + isPositive(number));
    }
}

class Stroka {
    public static String getGrade(int score) {
        if (score >= 0 && score <= 20) {
            return "E";
        } else  if (score >= 21 && score <= 40) {
            return "D";
        } else  if (score >= 41 && score <= 60) {
            return "C";
        } else  if (score >= 61 && score <= 80) {
            return "B";
        } else  if (score >= 81 && score <= 100) {
            return "A";
        } else {
            return "Error";
    }
    }
    public static void main(String[] args) {
        System.out.println("0: " + getGrade(0));
        System.out.println("20: " + getGrade(20));
        System.out.println("21: " + getGrade(21));
        System.out.println("60: " + getGrade(60));
        System.out.println("80: " + getGrade(80));
        System.out.println("100: " + getGrade(100));
        System.out.println("101: " + getGrade(101));
        System.out.println("-1: " + getGrade(-1));

    }
}

class Poexali {
    public static String blassOff(int start) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            result.append(i);

            if (i > 1) {
                result.append(", ");
            } else {
                result.append(" ");
            }
        }
        result.append("Поехали!");
        return result.toString();
    }
public static void main(String[] args) {
    System.out.println(blassOff(10));
    }
}


class Summa {
    public static int sumToN(int n) {
        int sum = 0;

        for (int i = 1; i <= n; i++) {
            sum +=i;
        }
        return sum;
    }
    public static void main(String[] args) {
        System.out.println(sumToN(5));
        System.out.println(sumToN(15));
    }
}

class Bug {
    public static Boolean hasBug(String[] messages) {
        for (String message : messages) {
            if ("Bug".equalsIgnoreCase(message)) {
                return true;
            }
        }
        return  false;
    }
    public static void main(String[] args) {
        String[] messages = {"Warning", "Error", "Bug"};
        System.out.println((hasBug(messages)));
    }
}

class Range {
    public static String getEvenInRange(int start, int end) {
        StringBuilder result = new StringBuilder();
        for (int i= start; i <= end; i++) {
            if (i % 2 == 0) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(i);
            }
        }
        return result.toString();
    }
    public static void  main(String[] args) {
        System.out.println(getEvenInRange(2, 5));
        System.out.println(getEvenInRange(1, 10));

    }
}

class findMax {
    public static  int findMax(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] numbers = {0,5,23,-24,4,99};
        System.out.println(findMax(numbers));
    }
}

class Reverse {
    public static String[] reverse(String[] arr) {
        String[] result = new String[arr.length];

        for (int i=0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }
        return result;
    }
    public static void main(String[] args) {
        String[] words = {"One", "Two", "Zero"};
        String[] reversed = reverse(words);

        System.out.println(String.join(", ", reversed));
    }

}

class Average {
    public static double calcAverage(List<Integer> list) {
        int sum = 0;

        for (int number : list) {
            sum += number;
        }
        return (double) sum / list.size();
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(10, 20, 30);
        System.out.println(calcAverage(numbers));

    }
}

class Names {
    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>();

        for (String name : list) {
            if (!name.equals(nameToRemove)) {
                result.add(name);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> names = List.of("Aleksei", "Ivan", "Anastasiya", "Petr");
        List<String> result = removeSpecificName(names, "Ivan");

        System.out.println(result);
    }
}