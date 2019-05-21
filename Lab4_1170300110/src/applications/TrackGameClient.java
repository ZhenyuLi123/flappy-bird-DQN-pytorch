package applications;

import java.io.File;
import java.io.IOException;


import APIs.PanelTest;
import circularOrbit.label;
import circularOrbit.word;
import exception.MyException;
import physicalObject.Athlete;
import track.Track;



public class TrackGameClient {
	public static void main(String[] args) throws IOException {

		File f = new File("src/circularOrbit/test/TrackGameOrder1.txt");
		TrackGameContext client = new TrackGameContext();

		TrackGame trackgame = new TrackGameSorted();
		
		try {
			trackgame.readFile(f);
		}catch(MyException e) {
			e.printInfo();
			//try again
			File f1 = new File("src/circularOrbit/test/TrackGame.txt");
			try {
				client = new TrackGameContext();
				trackgame = new TrackGameSorted();
				trackgame.readFile(f1);
			} catch (MyException e1) {
				e1.printInfo();
			} catch (IOException e1) {
				System.out.println("�ļ�Ŀ¼������");
			}
		}catch(IOException e) {
			System.out.println("�ļ�Ŀ¼������");
			File f1 = new File("src/circularOrbit/test/TrackGame.txt");
			try {
				client = new TrackGameContext();
				trackgame = new TrackGameSorted();
				trackgame.readFile(f1);
			} catch (MyException e1) {
				e1.printInfo();
			} catch(IOException e1) {
				System.out.println("�ļ�Ŀ¼������");
			}
		}
		

		client.run(trackgame);

		client.game.showResult();
		client.game.delATrack(5);
		client.game.addNewTrack(new Track(6));
		//name, num, nation, age, grade
		client.game.addObjectToTrack(client.game.tracks.get(2), 
				new Athlete(new word("lz"), 20, "CHN", 18, 10.01));
		client.game.change(client.game.objects.get(1), client.game.objects.get(3));
		client.game.showResult();
		
	}
}