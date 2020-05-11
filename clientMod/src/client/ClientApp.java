package client;

import java.io.IOException;

public class ClientApp {

    /**
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main(String[] args)  {
        System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");
        ClientProviding provide = new ClientProviding();
        provide.clientWork();
    }
}

