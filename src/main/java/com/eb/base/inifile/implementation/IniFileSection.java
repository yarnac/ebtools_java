package com.eb.base.inifile.implementation;

import java.util.ArrayList;
import java.util.List;

class IniFileSection {
	public IniFileSection(String l) {
		setName(l);
	}
	
	@Override
	public String toString() {
		return getName() + " " + getValues().size();
	}
	
	public void AddLine(String s)
	{
		if (s==null || s.trim().length()==0)
			return;
		getValues().add(s);
	}
	
	public String getValue(String value, String defaultString)
    {
        for (String str : getValues())
        {
            if (str.toUpperCase().startsWith(value.toUpperCase()+"="))
            {
                return getRealValue(str, defaultString).trim();
            }
			if (str.toUpperCase().startsWith("@"+value.toUpperCase() + "="))
			{
				return getRealValue(str, defaultString).trim(); 
			}
        }
        return defaultString;
    }
	
	 private String getRealValue(String str,String defaultString)
     {
         return getValuePair(str)[1];
     }
	 
	 private String[] getValuePair(String str)
     {
         int i = str.indexOf("=");
         int length = str.trim().length(); 
          
         
		if (i < 0 && length == 0)
             return null;

         String valueName = i < 0 ? str : str.substring(0, i);
         String valueValue = i < 0 ? "" : str.substring(i+1);
         return new String[]{valueName, valueValue};
     }
	 
	
	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	void setValues(List<String> values) {
		this.values = values;
	}

	private String name;
	private List<String> values = new ArrayList<String>();
	
	public void setValue(String topic, String defaultValue) {
		int i=0;
		for (String string : values) {
			if (string.startsWith(topic+"="))
			{				
				
				values.remove(string);
				values.add(i, topic+"="+defaultValue);
				
				return;
			}
			i++;
		}
		values.add(topic+"="+defaultValue);
		
	}

	public void WriteTo(StringBuilder strb) {
		strb.append(getName()+"\r\n");
		for (String string : values) {
			strb.append(string+"\r\n");
		}
		strb.append("\r\n");
		
		
	}
 
}
