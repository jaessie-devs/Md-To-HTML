import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

class md_to_itchio_html{
	
	private ArrayList<line> lines = new ArrayList<line>();
	private int current_open_lists = 0;
	
	
	/*Start the program on compiling*/
	
	public static void main(String args[]){
		new md_to_itchio_html();
	}
	
	public md_to_itchio_html(){
		start();
	}
	
	public void start(){
		String file_path = get_file_path();
		String file_text = get_file_text(file_path);
		ready_text_to_list(file_text);
		
		String converted_text = "";
		
		for(int index = 0; index < lines.size(); index ++){
			converted_text += setup_headings(lines.get(index));
		}
		
		System.out.println(converted_text);
		
	}
	
	
	/*Setup things to check the file text*/
	
	public String get_file_path(){
		String path = "";
		
		System.out.println("Enter File Path [Starting from root]");
		
		path = new Scanner(System.in).nextLine();
		
		return path.replace("\\", "/");
	}
	
	public String get_file_text(String file_path){
		String text = "";
		
		try{
			File file = new File(file_path);
			Scanner scan = new Scanner(file);
			while(scan.hasNext()){
				text += scan.nextLine() + '\n';
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println("Either the File doesn't exist or the path doesn't exist");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		text.replace(" ", "&nbsp");
		
		return text;
	}

	public void ready_text_to_list(String text){
		try{
			Scanner scan = new Scanner(text);
			
			while(scan.hasNext()){
				lines.add(new line(scan.nextLine() + '\n'));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/*Heading Checker*/
	
	public String setup_headings(line current_line){
		int last_hash_index = current_line.content.lastIndexOf('#');
		
		String part = "";
		
		if(last_hash_index < 0){
			return current_line.content;
		}
		
		else{
			part = current_line.content.substring(0, last_hash_index + 1);
		}
		
		
		if(last_hash_index > 5)
		{/*Checks if the heading number is above 6. There is no heading smaller than 6th*/}
		
		else if (current_line.content.charAt(last_hash_index + 1) != ' ')
		{/*Checks if there is at least one whitespace that sperate the heading from heading tag*/}
		
		else if (!part.matches("#+")){/**/}
		
		else{
			set_line_as_heading(last_hash_index + 1, current_line);
		}
		
		return current_line.content;
	}	
	
	private void set_line_as_heading(int hashes_number, line current_line){
		// hashes_number + 1: to eliminate whitespace that is after the # tag
		String pure_text = current_line.content.substring(hashes_number + 1);
		
		// Eliminate any soft/hard line break
		pure_text = pure_text.replace("\n", "");
		pure_text = pure_text.replace("\r", "");
		
		switch(hashes_number){
			case 1:
			current_line.formate = line.line_formates.H1;
			current_line.content = "<h1>" + pure_text + "</h1>\n";
			break;

			case 2:
			current_line.formate = line.line_formates.H2;
			current_line.content = "<h2>" + pure_text + "</h2>\n";
			break;

			case 3:
			current_line.formate = line.line_formates.H3;
			current_line.content = "<h3>" + pure_text + "</h3>\n";
			break;

			case 4:
			current_line.formate = line.line_formates.H4;
			current_line.content = "<h4>" + pure_text + "</h4>\n";
			break;

			case 5:
			current_line.formate = line.line_formates.H5;
			current_line.content = "<h5>" + pure_text + "</h5>\n";
			break;

			case 6:
			current_line.formate = line.line_formates.H6;
			current_line.content = "<h6>" + pure_text + "</h6>\n";
			break;
		}
	
	}

	
	/*Unordered List*/
	
	public String setup_unordered_list(line current_line){
		int first_symbol_index = current_line.content.indexOf('*');
		String text_before_symbol = "";
		String text_after_symbol = "";
		
		if(first_symbol_index < 0){
			first_symbol_index = current_line.content.indexOf('-');
			
			if(first_symbol_index < 0){
				return current_line.content;
			}
		}
		
		if(first_symbol_index > 0){
			text_before_symbol = current_line.content.substring(0, first_symbol_index);
			text_after_symbol = current_line.content.substring(first_symbol_index + 1);
		}
		else{
			text_before_symbol = "";
			text_after_symbol = current_line.content.substring(1);
		}
		
		if(text_before_symbol.equals("")){
			close_all_unordered_lists(current_line);
			open_unordered_list(current_line);
		}
		
		else if(!text_before_symbol.matches("\s+")){/*Checks for a*/}
		
		
		return current_line.content;
	}
	

	/*For readabilty reasons, I made functions for closing and opening an unordered list*/
	
	private void close_unordered_list(line current_line){
		current_line.content = "</ul>\n" + current_line.content;
		current_open_lists -= 1;
	}
	
	private void open_unordered_list(line current_line){
		current_line.content = "<ul>\n" + current_line.content;
		current_open_lists -= 1;
	}
	
	private void close_all_unordered_lists(line current_line){
		if (current_open_lists > 0){
			close_unordered_list(current_line);
			close_all_unordered_lists();
		}
	}
	
	/*Ordered List*/
	/*Code*/
	/*Quote*/
	/*Italic*/
	/*Bold*/
	/*Strikethrough*/
	/*Links*/
	/*Image*/
	/*Video*/

}


class line{
	public enum line_formates {H1, H2, H3, H4, H5, H6, NORMAL_TEXT, QUOTE, CODE}
	
	public String content = "";
	public line_formates formate = line_formates.NORMAL_TEXT;
	
	public line(String content){
		this.content = content;
	}
	
	public line(String content, line_formates formate){
		this.content = content;
		this.formate = formate;
	}
}