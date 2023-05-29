package oz.infra.io.tailer;
import java.io.File;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

public class App {

    private static final int SLEEP = 500;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.run();
    }

    private void run() throws InterruptedException {
        MyListener listener = new MyListener();
        Tailer tailer = Tailer.create(new File("/tmp/log.txt"), listener, SLEEP);
        while (true) {
            Thread.sleep(SLEEP);
        }
    }

    public class MyListener extends TailerListenerAdapter {

        @Override
        public void handle(String line) {
            System.out.println(line);
        }

    }
}
