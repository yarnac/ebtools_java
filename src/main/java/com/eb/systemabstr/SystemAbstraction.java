package systemabstr;

import java.util.List;
import java.util.Optional;
public interface SystemAbstraction {
	
	public static boolean hasUmlaute(List<String> lines) {
		Optional<String> findFirst 
		= lines.stream().filter( x->(3 + x.indexOf('ä') + x.indexOf('ü') + x.indexOf('ö')) > 0 ).findFirst();
		if (findFirst.isPresent())
		{
			findFirst.get().toString();
			return true;
		}
		return false;		
	}

	@SuppressWarnings("nls")
	public static String getStringNameOhneUmlaute(String fn) {
		return fn
				.replace("ö", "oe")
				.replace("ü","ue")
				.replace("ä", "ae")
				.replace("Ö", "Oe")
				.replace("Ü","Ue")
				.replace("Ä", "Ae")				
				.replace("ß", "ss");
	}
	
	
	
	

}
