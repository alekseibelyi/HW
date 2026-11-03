import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties properties = new Properties();


    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Извините, не удалось найти config.properties");
                throw new RuntimeException("config.properties not found in resources");
            }
            // Загружаем свойства из файла
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Ошибка при загрузке конфигурации", ex);
        }
    }

    // Методы для получения параметров
    public static String getStandUrl() { return properties.getProperty("stand.url"); }
    public static String getApiUrl() { return properties.getProperty("api.url"); }
    public static int getTimeout() { return Integer.parseInt(properties.getProperty("timeout")); }
    public static boolean isLoggingEnabled() { return Boolean.parseBoolean(properties.getProperty("logging.enabled")); }

    public static String getAdminLogin() { return properties.getProperty("admin.login"); }
    public static String getAdminPassword() { return properties.getProperty("admin.password"); }

    public static String getProductName() { return properties.getProperty("product.name"); }
    public static double getProductPrice() { return Double.parseDouble(properties.getProperty("product.price")); }

    // Метод для вывода конфигурации в консоль
    public static void printConfig() {
        System.out.println("=========================================");
        System.out.println("КОНФИГУРАЦИЯ ТЕСТОВ:");
        System.out.println("URL стенда: " + getStandUrl());
        System.out.println("URL API: " + getApiUrl());
        System.out.println("Тайм-аут (мс): " + getTimeout());
        System.out.println("Логирование: " + isLoggingEnabled());
        System.out.println("Стартовый товар: " + getProductName() + " (Цена: " + getProductPrice() + ")");
        System.out.println("=========================================");
    }
}