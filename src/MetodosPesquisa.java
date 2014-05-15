import java.io.BufferedReader;
import java.io.FileReader;  
import java.util.StringTokenizer; 
import java.util.Random;
import java.util.Arrays;
import java.io.*;  

public class MetodosPesquisa {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String Dados [][] = new String[21][12];
		int Solucao [] = new int [20];
		int TempoExecucaoTotal = 0;
		String texto = "";
	
		Dados = LeDados(Dados);
		       
        for(int i = 0; i < 21; i++, System.out.println("\n") )
            for(int j = 0; j < 12; j++) 
                System.out.print(Dados[i][j]+" ");
        
        for(int i = 1; i < 21; i++)  
            for(int j = 1; j < 2; j++)  
            	TempoExecucaoTotal = TempoExecucaoTotal + Integer.parseInt(Dados[i][j]);
        
        System.out.println("Tempo de execucao total de todos os casos de teste: " + TempoExecucaoTotal);
        
        Solucao = BuscaRandomSearch(Dados, 200);
        //Solucao = BuscaRandomSearch(Dados, 300);
        //Solucao = BuscaRandomSearch(Dados, 400);
        
        Solucao = BuscaHillClimbing(Dados, 200);
        //Solucao = BuscaHillClimbing(Dados, 300);
        //Solucao = BuscaHillClimbing(Dados, 400);
        
        System.out.print("Solução escolhida: ");
        texto = texto + "Solução escolhida: ";
        
        for(int i = 0; i < 20; i++) {
          System.out.print(Solucao[i] + " ");
          texto = texto + Solucao[i] + " ";
        }
        texto = texto + "\r\n";
        GravaDados(texto, false);
        
        texto = "";
        System.out.println("");
        System.out.print("Casos de teste da solução escolhida: ");
        texto = texto + "Casos de teste da solução escolhida: ";
        
        for(int i = 0; i < 20; i++) {
           if (Solucao[i] == 1) {
             System.out.print(Dados[i+1][0] + " ");
             texto = texto + Dados[i+1][0] + " ";
           }
        }
        texto = texto + "\r\n";
        GravaDados(texto, true);
    }
	
	public static String[][] LeDados(String matriz[][]) {
		
		BufferedReader leArquivo;
		StringTokenizer leLinhaArquivo;  
		int i = 0, j = 0; 
		boolean fimArquivo; 
		String nomeArquivo = "d:\\dados\\exercicio.txt";
				
		try {  
			System.out.println("Inicio da leitura do arquivo.");  
			
			leArquivo = new BufferedReader( new FileReader( nomeArquivo ) );
			
			int nInstancia = 0;
			String linhaArquivo; 
			fimArquivo = false; 
			
			while( true ) { 
				nInstancia++;
				
				for(i = 0; i < 21; i++){
					linhaArquivo = leArquivo.readLine();
					
					if( linhaArquivo == null ){
						fimArquivo = true; 
						break;
					}
					
					leLinhaArquivo = new StringTokenizer(linhaArquivo, "; ");
					
					for(j = 0; j < 12; j++){  
                        matriz[i][j] = leLinhaArquivo.nextToken();  
                    } 
				}
				
				if(fimArquivo)  break;
				 
				System.out.println("Matriz "+ nInstancia);  
			}
			
			System.out.println("Fim da leitura do arquivo.");  
			
		} catch( Exception erro ) {  
	            System.out.println("ERRO : " + erro.toString());  
		}
		
		return matriz;
	}

	public static int[] BuscaRandomSearch(String matriz[][], int L) {
		Random gerador = new Random();
		int FitnessAtual = 0;
		int SolucaoAtual [] = new int [20];
		int SolucaoCandidata [] = new int [20];
		int TempoExecucao;
		int Cobertura;
		String texto;
		
		boolean achei = false;
		int ns = 0;
		while (achei == false) {
			TempoExecucao = 0;
			Cobertura = 0;
			texto = "";
			
			for (int i = 0; i < 20; i++) {
				SolucaoCandidata[i] = gerador.nextInt(2); 
	        	System.out.print(SolucaoCandidata[i]+" ");
	        	texto = texto + SolucaoCandidata[i]+" ";
			}
			
			for (int i = 0; i < 20; i++) {
				if (SolucaoCandidata[i] == 1) {
					TempoExecucao = TempoExecucao + Integer.parseInt(matriz[i+1][1]);
					
					//for (int j = 2; j < 12; j++) 
				    //	Cobertura = Cobertura + (Integer.parseInt(matriz[i+1][j]) * 1);
					
					Cobertura = Cobertura + Fitness(TempoExecucao, i+1, matriz);
					//FitnessResultado = Fitness(TempoExecucao, SolucaoCandidata, matriz);
			    }
			}
        
			System.out.print("Tempo de execucao da solucao candidata: " + TempoExecucao);
			texto = texto + "Tempo de execucao da solucao candidata: " + TempoExecucao;
			
			System.out.print(" Cobertura da solucao candidata: " + Cobertura);
			texto = texto + " Cobertura da solucao candidata: " + Cobertura;
								
			if ((Cobertura > FitnessAtual) && (TempoExecucao <= L)) {
				FitnessAtual = Cobertura;
			    SolucaoAtual = Arrays.copyOf(SolucaoCandidata,SolucaoCandidata.length);
			    
			    System.out.print(" ***** Solucao Atual ***** ");
			    texto = texto + " ***** Solucao Atual ***** ";
	    	}
			
			if ((ns > 600)) {  //(TempoExecucao == L) ||
			   achei = true;
			}
			
			ns++;
			System.out.println(" Numero da solucao candidata: " + ns);
			texto = texto + " Numero da solucao candidata: " + ns;
			texto = texto + "\r\n";
						
			GravaDados(texto, false);
		}
		
		return SolucaoAtual;
	}
	
	public static int[] BuscaHillClimbing(String matriz[][], int L) {
		Random gerador = new Random();
		int FitnessAtual = 0;
		int SolucaoAtual [] = new int [20];
		int SolucaoCandidata [] = new int [20];
		int SolucaoVizinha [] = new int [20];
		int TempoExecucao;
		int Cobertura;
		String texto;

		boolean achei = false;
		int ns = 1;
		
		while (achei == false) {
			TempoExecucao = 0;
			Cobertura = 0;
			texto = "";
			
			// gera a solucao candidata
			for (int i = 0; i < 20; i++) {
				SolucaoCandidata[i] = gerador.nextInt(2); 
				System.out.print(SolucaoCandidata[i]+" ");
				texto = texto + SolucaoCandidata[i]+" ";
			}
			
			// calcula dados da solucao
			for (int i = 0; i < 20; i++) {
				if (SolucaoCandidata[i] == 1) {
					TempoExecucao = TempoExecucao + Integer.parseInt(matriz[i+1][1]);
					
					Cobertura = Cobertura + Fitness(TempoExecucao, i+1, matriz);
			    }
			}
        
			System.out.print("Tempo de execucao da solucao candidata: " + TempoExecucao);
			texto = texto + "Tempo de execucao da solucao candidata: " + TempoExecucao;
			
			System.out.print(" Cobertura da solucao candidata: " + Cobertura);
			texto = texto + " Cobertura da solucao candidata: " + Cobertura;
					
			// analisa cobertura solucao candidata
			if ((Cobertura > FitnessAtual) && (TempoExecucao <= L)) {
				FitnessAtual = Cobertura;
			    SolucaoAtual = Arrays.copyOf(SolucaoCandidata,SolucaoCandidata.length);
			    
			    System.out.print(" ***** Solucao Atual ***** ");
			    texto = texto + " ***** Solucao Atual ***** ";
	    	}
			
			System.out.println(" Numero da solucao candidata: " + ns);
			texto = texto + " Numero da solucao candidata: " + ns;
			texto = texto + "\r\n";
			
			GravaDados(texto, false);
			
			// se encontrou uma solucao candidata
			if (FitnessAtual > 0) {
                int v = 0;
				// analisando as soluções vizinhas da solucao candidata
				while (v < 20) {
		       		TempoExecucao = 0;
					Cobertura = 0;
					texto = "";
									
					//gera soluçao vizinha mudando 1 bit da solucao atual
					SolucaoVizinha = Arrays.copyOf(SolucaoAtual,SolucaoAtual.length);
				
					if (SolucaoVizinha[v] == 1) {
						SolucaoVizinha[v] = 0;
					}
					else {
						SolucaoVizinha[v] = 1;
					}
				
					System.out.print("");
					
					//mostra a solucao vizinha gerada
					for (int i = 0; i < 20; i++) {
						System.out.print(SolucaoVizinha[i]+" ");
						texto = texto + SolucaoVizinha[i]+" ";
					}
								
					// analisa solucao vizinha
					for (int i = 0; i < 20; i++) {
						if (SolucaoVizinha[i] == 1) {
							TempoExecucao = TempoExecucao + Integer.parseInt(matriz[i+1][1]);
											
							Cobertura = Cobertura + Fitness(TempoExecucao, i+1, matriz);
						}
					}
	        
					System.out.print("Tempo de execucao da solucao vizinha: " + TempoExecucao);
					texto = texto + "Tempo de execucao da solucao vizinha: " + TempoExecucao;
					
					System.out.print(" Cobertura da solucao vizinha: " + Cobertura);
					texto = texto + " Cobertura da solucao vizinha: " + Cobertura;
									
					if ((Cobertura > FitnessAtual) && (TempoExecucao <= L)) {
						FitnessAtual = Cobertura;
						SolucaoAtual = Arrays.copyOf(SolucaoAtual,SolucaoAtual.length);
				    
						System.out.print(" ***** Solucao Atual ***** ");
						texto = texto + " ***** Solucao Atual ***** ";
					}
		       
					v++;
					System.out.println(" Numero da solucao vizinha: " + v);
					texto = texto + " Numero da solucao vizinha: " + v;
					texto = texto + "\r\n";
					
					GravaDados(texto, false);
				}
		    }
		
		    if ((ns > 600)) {  //(TempoExecucao == L) ||
		      achei = true;
		    }
		
		    ns++;
		    // System.out.println(" Numero da solucao candidata: " + ns);
		}
		
		return SolucaoAtual;
	}
	
	public static int Fitness(int TempoExec, int Pos, String matriz[][]) {
		int valorCobertura = 0;
				
		for (int j = 2; j < 12; j++) 
			valorCobertura = valorCobertura + (Integer.parseInt(matriz[Pos][j]) * 1);
		
		return valorCobertura;
	}
	
    public static void GravaDados(String texto, boolean fimArquivo) {
			
		try {  
			// System.out.println("Inicio da gravacao do arquivo.");
			
			File arquivo;  
	    
			arquivo = new File("d:\\dados\\resultado.txt");  
			
            FileOutputStream fos = new FileOutputStream(arquivo, true); 
            
            //texto = texto + "\r\n";
            
            fos.write(texto.getBytes());  

			if (fimArquivo == true) {
				fos.close(); 
			}
					
			// System.out.println("Fim da gravacao do arquivo."); 
			
		} catch( Exception erro ) {  
	            System.out.println("ERRO : " + erro.toString());  
		}
		
	}

}
