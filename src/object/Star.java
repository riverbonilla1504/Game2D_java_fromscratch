package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Star extends SuperObject{
    public Star(){
        Name = "Star";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/main/objects/star1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
