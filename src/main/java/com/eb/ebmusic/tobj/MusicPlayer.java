package com.eb.ebmusic.tobj;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.eb.base.MainGlobals;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import com.eb.base.io.FileUtil;

import javazoom.jl.player.Player;

public class MusicPlayer {
		
	private Player player;
	private Thread actThread;
	private boolean stop;
	private Consumer<String> showActTitle;
	
	
	public void play(String fileName )
	{
		List<String>  filesToPlay = new ArrayList<>();
		play(filesToPlay);
	}
	
	
	int actTitleNr;
	

	public void play(List<String> filenamesToPlay) {
		stop();
		if (filenamesToPlay.stream().filter(x->!x.toLowerCase().endsWith(".mp3")).count()>0)
		{
			playUsingVlc(filenamesToPlay);
			return;
		}

		actThread = new Thread() {
			@Override
			  public void run() {	
				  actTitleNr=0;
				  for (actTitleNr=0; actTitleNr<filenamesToPlay.size(); actTitleNr++) {
					  String file = filenamesToPlay.get(actTitleNr);
					  showTitle(file);					  
					  if (!playPrivate(file))
						  break;
				  }													  			 
			  }			
			};
		actThread.start();

		FileUtil.appendLine("","");
		
	}

	private void playUsingVlc(List<String> filenamesToPlay) {
		if (MainGlobals.isWindows())
			playUsingVlcWindows(filenamesToPlay);
		else
			plaxyUsingVlcMacOs(filenamesToPlay);
	}

	private static void plaxyUsingVlcMacOs(List<String> filenamesToPlay) {
		try {
			// macOS nutzt das "open"-Kommando, um Apps zu starten
			List<String> command = new ArrayList<>();
			command.add("open");
			command.add("-a");
			command.add("VLC");

			StringBuilder strb = new StringBuilder();
			for(String filename : filenamesToPlay) {
				strb.append(filename);
				strb.append(" ");
			}
			//command.add(strb.toString());
			// Alle Dateien anhängen
			command.addAll(filenamesToPlay);

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void playUsingVlcWindows(List<String> filenamesToPlay) {
		try {
			// Pfad zur VLC-Executable (an dein System anpassen!)
			String vlcPath = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";

			// Befehl zusammenbauen
			ProcessBuilder pb = new ProcessBuilder();
			pb.command().add(vlcPath);

			// Alle Dateien hinzufügen
			pb.command().addAll(filenamesToPlay);

			// Prozess starten
			pb.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private boolean playPrivate(String file) {
		
		
		if (fileHasSuffix(file,".mp3"))			  			 
			return playMP3(file);
		if (fileHasSuffix(file,".m4a"))			  			 
			return playM4A(file);
		else if (fileHasSuffix(file, ".wav"))
			return playWAV(file);
		else 
			return false;
	}

	private boolean playM4A(String filePath) {
		Media media = new Media(filePath.replace('\\','/'));
		MediaPlayer mediaPlayer = new MediaPlayer(media);

		mediaPlayer.play(); // Start playing
		return true;

	}


	private boolean playWAV(String file) {

		return false;
	}



	private boolean playMP3(String file) {
/*
		String str = file.substring(40);
		Media media = new Media(file.replace('\\', '/'));
		MediaPlayer player2 = new MediaPlayer(media);
		player2.play();
		*/
		
		
		
		try(FileInputStream fis = new FileInputStream(file)){		
			player = new Player(fis);						  
			  player.play();
			  player.close();	
			  player = null;
			  if (stop)
				  return false;
			  else
				  return true;	  			  			  			 
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return false;					
	}



	private boolean fileHasSuffix(String file, String suffix) {
		return file.toLowerCase().endsWith(suffix);
	}


	public Consumer<String> getShowActTitle() {
		return showActTitle;
	}



	public void setShowActTitle(Consumer<String> showActTitle) {
		this.showActTitle = showActTitle;
	}



	public void stop() {
		if (player!=null)
		{
			stop=true;
			player.close();
			if (actThread.isAlive())
				actThread.stop();
			stop = false;
			
		}
			
	}



	private void showTitle(String file) {
		
		String name = FileUtil.getFileName(file) + " (" + FileUtil.getFileName(FileUtil.getParentdirectory(file)) + ")";
		
		if (getShowActTitle()!=null)
			  getShowActTitle().accept(name);
		
	}



	public void playPreviousTrack() {
		if (actTitleNr>0)
		{
			actTitleNr-=2;
			if (player!=null)
				player.close();
		}
	}



	public void playNextTrack() {
		if (player!=null)
			player.close();
		
	}


	public void pause() {
		if (player!=null)
			player.toString();
		
	}
}
