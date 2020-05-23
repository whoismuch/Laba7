package client;

import java.io.IOException;

public class ClientApp {

    /**
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main(String[] args)  {
        try {
            ClientProviding provide = new ClientProviding(args[0], args[1]);
            System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");
            provide.clientWork( );
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Все оооочень просто, смарите\nАдрес *пробел* Порт" );
        }

    }
}

