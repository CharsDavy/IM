/**
 * 媒体播放
 */

package setting;

import java.io.FileNotFoundException;
import java.io.IOException;
import sun.audio.AudioStream;

/**
 *
 * @author 
 */
public class AudioPlay
{


	private static AudioStream play_music;

	public  void soundPlay(String sound)
	{
		try
		{

			play_music = new AudioStream(this.getClass().getResourceAsStream(sound));

			sun.audio.AudioPlayer.player.start(play_music);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e){}
	}
}
