package model;

public class Parser {

	// create or drop database or table
	String x1 = "([C|c][R|r][E|e][A|a][T|t][E|e]|[D|d][R|r][O|o][P|p])\\s+[D|d][A|a][T|t][A|a][B|b][A|a][S|s][E|e]\\s+[A-Za-z0-9_.]+\\s*[;]\\s*";
	String y1 = "[D|d][R|r][O|o][P|p]\\s+[T|t][A|a][B|b][L|l][E|e]\\s+[A-Za-z0-9._]+\\s*[;]\\s*";
	String f1 = "[[A-Za-z0-9._]+\\s+(int|varchar)\\s*[,]?\\s*]+[)]\\s*[;]\\s*";
	String z1 = "[C|c][R|r][E|e][A|a][T|t][E|e]\\s+[T|t][A|a][B|b][L|l][E|e]\\s+[A-Za-z0-9._]+\\s+[(]\\s*" + f1;

	public String create_dropCheck(String query) {
		String validQuery = null;
		if (query.matches(x1) || query.matches(y1) || query.matches(z1)) {
			validQuery = addSpaces(query);
			
		}
		return validQuery;
	}

	// Insert into syntax first way
	String a1 = "[I|i][N|n][S|s][E|e][R|r][T|t]\\s+[I|i][N|n][T|t][O|o]\\s+[A-Za-z0-9]+\\s+[(]\\s*";
	String b1 = "[[A-Za-z0-9._]+\\s*[,]?\\s*]+[)]\\s*";
	String c1 = "[V|v][A|a][L|l][U|u][E|e][S|s]\\s+[(]\\s*[['][A-Za-z0-9._ ]+[']\\s*[,]?\\s*]+[)]\\s*[;]\\s*";

	// Insert into syntax second way
	String a2 = "[I|i][N|n][S|s][E|e][R|r][T|t]\\s+[I|i][N|n][T|t][O|o]\\s+[A-Za-z0-9._ ]+\\s+";
	String b2 = c1;

	
	
	
	
	// Delete from table without condition
	String f3 = "[D|d][E|e][L|l][E|e][T|t][E|e]\\s+[F|f][R|r][O|o][M|m]\\s+[A-Za-z0-9._]+\\s*[;]\\s*";
	// Delete from table with condition
	String x3 = "[D|d][E|e][L|l][E|e][T|t][E|e]\\s+[F|f][R|r][O|o][M|m]\\s+[A-Za-z0-9._]+";
	// condition
	String y3 = "\\s+[W|w][H|h][E|e][R|r][E|e]\\s+[A-Za-z0-9._ ]+\\s*(=|<|>)\\s*[']?[A-Za-z0-9._ ]+[']?\\s*[;]\\s*";
	// DELET * FROM table
	String z3 = "[D|d][E|e][L|l][E|e][T|t][E|e]\\s+[*]\\s+[F|f][R|r][O|o][M|m]\\s+[A-Za-z0-9._]+\\s*[;]\\s*";

	
	
	// update with condition
	String a3 = "[U|u][P|p][D|d][A|a][T|t][E|e]\\s+[A-Za-z0-9._]+\\s+[S|s][E|e][T|t]\\s+";
	String b3 = "[[A-Za-z0-9._]+\\s*[=]\\s*['][A-Za-z0-9._ ]+[']\\s*[,]?\\s*]+";
	// condition
	String c3 = y3;
	// update without condition
	String d3 = "[[A-Za-z0-9._]+\\s*[=]\\s*['][A-Za-z0-9._ ]+[']\\s*[,]?\\s*]+\\s*[;]\\s*";

	
	
	public String insertCheck(String query) {
		String validQuery = null;

		if (query.matches(a1 + b1 + c1) || query.matches(a1 + c1) || query.matches(a2 + b2) || query.matches(f3)
				|| query.matches(x3 + y3) || query.matches(z3) || query.matches(a3+b3+c3) || query.matches(a3+d3)) {
			validQuery = addSpaces(query);
		}
		return validQuery;
	}
	
		

	// select from table
	String x2 = "[S|s][E|e][L|l][E|e][C|c][T|t]\\s+[[A-Za-z0-9._]+\\s*[,]?\\s*]+\\s+[F|f][R|r][O|o][M|m]\\s+[A-Za-z0-9._]+\\s*[;]\\s*";
	// SELECT * FROM table
	String y2 = "[S|s][E|e][L|l][E|e][C|c][T|t]\\s+[*]\\s+[F|f][R|r][O|o][M|m]\\s+[A-Za-z0-9._]+\\s*[;]\\s*";
	
	public String selsctCheck(String query) {
		String validQuery = null;
		if (query.matches(x2) || query.matches(y2) ) {
			validQuery = addSpaces(query);
		}
		return validQuery;
	}
	

	public String addSpaces(String query) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < query.length(); i++) {
			char c = query.charAt(i);
			if (c == '=' || c == ',' || c == '(' || c == ')' || c == '\'') {
				stringBuilder.append(" ");
				stringBuilder.append(c);
				stringBuilder.append(" ");

			} else {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();

	}
}
