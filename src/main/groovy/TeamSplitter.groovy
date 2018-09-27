
import static com.xlson.groovycsv.CsvParser.parseCsv
import com.google.common.collect.*;
import java.util.prefs.Preferences
import javax.swing.*;

public class TeamSplitter {

	public static void main(String[] args) {
		if(args.length == 0){
			//print command line directions
			println "To run from command line, supply with file path, and column to split on as arguments"
		}
		
		String filePath = null;
		if(args.length <= 0){
			Preferences prefs = Preferences.userNodeForPackage(TeamSplitter.class);
			String filePathKey = "FILE_LOAD_PATH";
			
			String savedPath = prefs.get(filePathKey, "");
			
			JFileChooser jfc = new JFileChooser(savedPath);
			jfc.setDialogTitle("Pick CSV file to split up");
			jfc.showOpenDialog(null);
			filePath = jfc.getSelectedFile().getAbsolutePath();
			
			prefs.put(filePathKey, filePath);
			prefs.flush();
		}
		else{
			filePath = args[0];
		}

		if(filePath == null){
			return;
		}

		File f = new File(filePath);
		def lines = f.readLines();
		String header = lines[0];

		String bibColumn = null;
		if(args.length <= 1){				
			bibColumn = JOptionPane.showInputDialog(null, "Pick bib column", "Pick bib column",
				JOptionPane.INFORMATION_MESSAGE, null, lines[0].split(","), null);
		}
		else{
			bibColumn = args[1];
		}
		
		if(bibColumn == null){
			return;
		}

		Multimap<String, String> bibToRunners = ArrayListMultimap.create();

		def data = parseCsv(f.getText())
		int row = 1;
		for(processedLine in data){
			bibToRunners.put(processedLine[bibColumn], lines[row]);
			println "Put: "+processedLine[bibColumn]+lines[row];
			row++;
		}

		int fileNum = 1;
		while(!bibToRunners.isEmpty()){
			File outFile = new File(f.getAbsolutePath()+".processed_"+fileNum+".csv");
			outFile.write(header+"\r\n");
			
			//write all blank entries to first file
			for(String value: new ArrayList(bibToRunners.get(""))){
				outFile.append(value+"\r\n");
				bibToRunners.remove("", value);
			}
			
			for(String bib: new ArrayList(bibToRunners.keySet())){
				String value = bibToRunners.get(bib)[0];
				outFile.append(value+"\r\n");
				bibToRunners.remove(bib, value);
			}
			fileNum++;
		}
	}

}
