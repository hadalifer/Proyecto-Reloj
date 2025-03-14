import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class RelojAnalogico extends JPanel {
    private BufferedImage buffer; // Doble búfer
    private int width = 400, height = 400;
    private ImageIcon paquita1;
    private ImageIcon paquita2;
    private ImageIcon paquita3;
    private ImageIcon paquita4;
    private ImageIcon manecillaSegundo;
    private ImageIcon manecillaMinutos;
    private ImageIcon manecillaHoras;

    public RelojAnalogico() {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Cargar la imagen
        paquita1 = new ImageIcon("paquita1.png");
        paquita2 = new ImageIcon("paquita2.png");
        paquita3 = new ImageIcon("paquita3.png");
        paquita4 = new ImageIcon("paquita4.png");
        manecillaSegundo = new ImageIcon("segundero.png");
        manecillaMinutos = new ImageIcon("minutos.png");
        manecillaHoras = new ImageIcon("horas.png");
        
        // Verificar si la imagen se cargó correctamente
        if (paquita1.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }
        // Verificar si la imagen se cargó correctamente
        if (paquita2.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }
        // Verificar si la imagen se cargó correctamente
        if (paquita3.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }
        // Verificar si la imagen se cargó correctamente
        if (paquita4.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }
        // Verificar si la imagen se cargó correctamente
        if (manecillaSegundo.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }
        // Verificar si la imagen se cargó correctamente
        if (manecillaMinutos.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }
        // Verificar si la imagen se cargó correctamente
        if (manecillaHoras.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Error: No se pudo cargar la imagen ");
        }

        // Actualiza el reloj cada segundo usando un hilo
        Executors.newSingleThreadScheduledExecutor().execute(this::startClock);
    }


    private void startClock() {
        Timer timer = new Timer(true);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            private int lastHour = LocalTime.now().getHour(); // Guardamos la última hora registrada
    
            @Override
            public void run() {
                LocalTime now = LocalTime.now();
                int currentHour = now.getHour();
                
                playTickSound(); // Sonido del segundero
                repaint(); // Redibujar el reloj
    
                // Si la hora cambia, reproducimos el sonido de la hora
                if (currentHour != lastHour) {
                    playHourSound();
                    lastHour = currentHour; // Actualizamos la hora registrada
                }
            }
        }, 0, 1000);
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Limpiar fondo
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        
        

        // Dibujar el reloj
        g2.setColor(new Color(255,247,251));
        g2.fillOval(23, 23, 350, 350);
        g2.setColor(new Color(171,85,130));
        g2.drawOval(23, 23, 350, 350);

        //dibujar las marcas del reloj
        drawClockMarks(g2); 
        
        // Dibujar la imagen de fondo
        if (paquita1.getImageLoadStatus() == MediaTracker.COMPLETE) {
            g2.drawImage(paquita1.getImage(), 155, 15, 90, 90, this);
        }
        if (paquita2.getImageLoadStatus() == MediaTracker.COMPLETE) {
            g2.drawImage(paquita2.getImage(), 297, 155, 80, 80, this);
        }
        if (paquita3.getImageLoadStatus() == MediaTracker.COMPLETE) {
            g2.drawImage(paquita3.getImage(), 160, 295, 80, 80, this);
        }
        if (paquita4.getImageLoadStatus() == MediaTracker.COMPLETE) {
            g2.drawImage(paquita4.getImage(), 9, 150, 90, 90, this);
        }
        // Obtener hora del sistema
        LocalTime now = LocalTime.now();
        int hours = now.getHour() % 12;
        int minutes = now.getMinute();
        int seconds = now.getSecond();
        
        // Dibujar manecillas


        double tercerAngulo = hours * 30;/* tiempo de giro  */
        drawRotatedImage(g2, manecillaHoras, 191, 202, tercerAngulo);
        double segundoAngulo = minutes * 6;/* tiempo de giro  */
        drawRotatedImage(g2, manecillaMinutos, 190, 202, segundoAngulo);
        double primerAngulo = seconds * 6.0;/* tiempo de giro  */
        drawRotatedImage(g2, manecillaSegundo, 193, 202, primerAngulo);
        

        // Mostrar el doble búfer
        g.drawImage(buffer, 0, 0, null);
        g2.dispose();
    }

   /*  private void drawHand(Graphics2D g, int x, int y, int length, double angle, Color color) {
        double rad = Math.toRadians(angle - 90);
        int xEnd = x + (int) (length * Math.cos(rad));
        int yEnd = y + (int) (length * Math.sin(rad));
        g.setColor(color);
        g.setStroke(new BasicStroke(3));
        g.drawLine(x + 50, y + 50, xEnd + 50, yEnd + 50);
    } */
    private void drawClockMarks(Graphics2D g2) {
        int centerX = 200, centerY = 200;
        int radius = 165;
        
        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6);
            int x1 = (int) (centerX + radius * Math.cos(angle));
            int y1 = (int) (centerY + radius * Math.sin(angle));
            int x2 = (int) (centerX + (radius - (i % 5 == 0 ? 15 : 5)) * Math.cos(angle));
            int y2 = (int) (centerY + (radius - (i % 5 == 0 ? 15 : 5)) * Math.sin(angle));
            g2.setStroke(new BasicStroke(i % 5 == 0 ? 3 : 1));
            g2.setColor(new Color(171,85,130));
            g2.drawLine(x1, y1, x2, y2);
        }
    }
    private void drawRotatedImage(Graphics2D g2, ImageIcon image, int x, int y, double angle) {
        BufferedImage img = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        image.paintIcon(null, g, 0, 0);
        g.dispose();
    
        int width = img.getWidth();
        int height = img.getHeight();
        int centerX = width / 4;
        int centerY = -0 ; // Punto de giro en la base
    
        g2.translate(x, y);
        g2.rotate(Math.toRadians(angle), centerX, centerY);
        g2.drawImage(img, -centerX, -height, this); // Mover hacia arriba
        g2.rotate(Math.toRadians(-angle), centerX, centerY);
        g2.translate(-x, -y);
    }
    private void playTickSound() {
        try {
            File soundFile = new File("tick.wav"); // Ruta del archivo de sonido
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    private void playHourSound() {
        try {
            File soundFile = new File("hora.wav"); // Ruta del archivo de sonido
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Reloj Analógico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 420);
        frame.setResizable(false);
        frame.add(new RelojAnalogico());
        frame.setVisible(true);
    }
}
