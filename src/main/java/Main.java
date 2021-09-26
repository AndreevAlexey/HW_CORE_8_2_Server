import Dialog.DialogQuestions;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    // проверка корректности ответа клиента на вопрос сервера
    private static boolean checkAnswer(DialogQuestions question, String answer) {
        boolean result = true;
        switch(question) {
            case OLDER18:
            case MALE:
                if(!"y".equalsIgnoreCase(answer) && !"n".equalsIgnoreCase(answer)) {
                    result = false;
                }
                break;
        }
        return result;
    }

    public static void main(String[] args) {
        int port = 28989;
        // вопросы
        DialogQuestions[] questions = {DialogQuestions.NAME, DialogQuestions.OLDER18, DialogQuestions.MALE };
        // ответы
        HashMap<DialogQuestions, String> answers = new HashMap<>();
        int clientPort;
        // начало работы сервера
        System.out.println("Serever started "+System.currentTimeMillis());
        while(true) {
            // потоки
            try (ServerSocket serverSocket = new ServerSocket(port);
                 Socket clientSocket = serverSocket.accept(); // ожидание подключения клиента
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ){
                // порт клиента
                clientPort = clientSocket.getPort();
                System.out.println("New connection port = " + clientPort);
                out.println(clientPort);
                // начало диалога с клиентом
                String resp;
                for (int i = 0; i < questions.length; i++) {
                    // отправка вопроса клиенту
                    out.println(questions[i]);
                    // получение ответа
                    resp = in.readLine();
                    // проверка ответа
                    if (checkAnswer(questions[i], resp))
                        // добавляем
                        answers.put(questions[i], resp);
                    else
                        // повтор вопроса
                        i--;
                }
                // отправка признака конца диалога
                out.println("End dialog");
                // результат: обработка ответов клиента
                if ("y".equalsIgnoreCase(answers.get(DialogQuestions.OLDER18))
                        && "y".equalsIgnoreCase(answers.get(DialogQuestions.MALE))) {
                    out.println("Welcome to our site, " + answers.get(DialogQuestions.NAME));
                } else out.println("This site is not for you, " + answers.get(DialogQuestions.NAME));
                // чистим ответы
                answers.clear();
            } catch(IOException exp) {
                exp.printStackTrace();
            }
        }
    }
}
