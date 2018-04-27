package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {
	
	public static File generateReport (String text) {
		String name = "output";
		Process p;
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				
		String content = "\\documentclass[a4paper,10pt,landscape]{article}\n" + 
				"\\usepackage[utf8]{inputenc}\n" + 
				"\\usepackage{adjustbox}\n" + 
				"\\usepackage{geometry}\n" + 
				"\\usepackage[absolute,overlay]{textpos}\n" + 
				"\\geometry{a4paper,right=0mm,left=0mm,top=10mm,bottom=0mm}\n" + 
				"\\begin{document}\n" + 
				"\\thispagestyle{empty}\n" +
				"\\begin{adjustbox}{angle=0,keepaspectratio,center}\n" + 
				"  \\begin{tabular}{|l|l|l|l|l|l|l|l|}\n" +
				"	 \\multicolumn{8}{l}{\\textbf{\\huge{Histórico escolar}}} \\\\\n" + 
				"    \\multicolumn{8}{l}{} \\\\\n" +
				"    \\hline\n" + 
				"    \\textbf{Sigla} & \\textbf{Disciplina} & \\textbf{Período} & \\textbf{Aprovado} & \\textbf{Média Final} & \\textbf{Frequ\\^encia (\\%)} & \\textbf{Qtd.Faltas} & \\textbf{Observa\\c{c}\\~ao} \\\\\n" + 
				"    \\hline\n" + 
				text +  
				"    \\hline\n" +
				"\\multicolumn{8}{l}{} \\\\[-0.5em]" +
				"    \\multicolumn{8}{l}{Esse documento foi gerado automáticamente com dados disponíveis no Sistema Integrado de Gestão Acadêmica do Centro Paula Souza. Acesso em: " + date.format(new Date()) + "}\n" + 
				"  \\end{tabular}\n" + 
				"\\end{adjustbox}\n" + 
				"\\vspace*{\\fill}\n" + 
				"\\end{document}";
		String path = name + ".tex";
		
		try {
			Files.write( Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);
			p = Runtime.getRuntime().exec("pdflatex -synctex=1 -interaction=nonstopmode ./" + path);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new File(name + ".pdf");
	}
}
