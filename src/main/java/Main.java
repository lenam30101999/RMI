import control.IRegistration;
import lombok.SneakyThrows;
import model.Answer;
import model.Student;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        IRegistration service = (IRegistration) Naming.lookup("rmi://10.170.77.34:1099/Server");
        Student student = new Student("B17DCCN436", "Le Vu Nam", "10.170.77.38.", 2);
        Student response = service.register(student);
        System.out.println(response.toString());

        int ucln = USCLN(response.getNumericCode3(), response.getNumericCode4());
        int bcnn = BSCNN(response.getNumericCode3(), response.getNumericCode4(), ucln);
        int max = getMaxValue(response.getNumericExam());

        Answer answer = new Answer();
        answer.setStudent(response);
        answer.setReverseStringAnswer(reverseString(response.getStrExamCode1()));
        answer.setNormalizationStringAnswer(capitalize(response.getStrExamCode2()));
        answer.setUclnNumericAnswer(ucln);
        answer.setBcnnNumericAnswer(bcnn);
        answer.setMaxNumericAnswer(max);

        Answer result = service.answer(answer);
        System.out.println(result.toString());
    }

    public static String reverseString(String s){
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        builder = builder.reverse();
        return builder.toString();
    }

    public static String capitalize(String s){
        List<String> list = Arrays.asList(s.split(" "));
        String result = list.stream()
                .map(Main::capitalizeString)
                .collect(Collectors.joining(" "));

        System.out.println("RESULT: " + result);
        return result;
    }

    public static String capitalizeString(String s){
        String s1 = s.substring(0, 1).toUpperCase();
        return s1 + s.substring(1);
    }

    public static int USCLN(int a, int b) {
        if (b == 0) return a;
        return USCLN(b, a % b);
    }

    public static int BSCNN(int a, int b, int c) {
        return (a * b) / c;
    }

    public static int getMaxValue(int[] numbers){
        int maxValue = numbers[0];
        for(int i=1;i < numbers.length;i++){
            if(numbers[i] > maxValue){
                maxValue = numbers[i];
            }
        }
        return maxValue;
    }
}
