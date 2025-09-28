import java.util.*;
interface MediaPlayer { 
    void play(String filename); 
}

class Mp3Player implements MediaPlayer {
    public void play(String filename){ 
        System.out.println("Playing mp3: " + filename); 
    }
}

class Mp4Player {
    public void playMp4(String filename){ 
        System.out.println("Playing mp4: " + filename); 
    }
}

class Mp4Adapter implements MediaPlayer {
    private Mp4Player mp4 = new Mp4Player();
    
    public void play(String filename){ 
        mp4.playMp4(filename); 
    }
}

public class AdapterDemo {
    public static void main(String[] args) {
        MediaPlayer mp3 = new Mp3Player();
        MediaPlayer mp4 = new Mp4Adapter();
        
        mp3.play("song.mp3");
        mp4.play("movie.mp4");
    }
}
