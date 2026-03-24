package com.eb.ebookreader.tobj;

public class EbBookStructureAnalyzer {

	private String textLeft;
	private String textRight;
	private String[] linesLeft;
	private String[] linesRight;

	public EbBookStructureAnalyzer(String _textLeft, String _textRight) {
		setTextLeft(_textLeft);
		setTextRight(_textRight);
		linesLeft = _textLeft.split("\\.");
		linesRight = _textRight.split("\\.");
		
		int n = linesLeft.length/2;
		StringBuilder strb1 = new StringBuilder();
		StringBuilder strb2 = new StringBuilder();
		for (int i=0; i+n<linesLeft.length  && i+n<linesRight.length && i<100;i++)
		{			
			String s = linesLeft[i+n];
			String t =  linesRight[i+n];
			
			strb1.append(s);
			strb2.append(t);					
		}
		
		this.toString();
		
	}

	public String getTextLeft() {
		return textLeft;
	}

	public void setTextLeft(String textLeft) {
		this.textLeft = textLeft;
	}

	public String getTextRight() {
		return textRight;
	}

	public void setTextRight(String textRight) {
		this.textRight = textRight;
	}

}
