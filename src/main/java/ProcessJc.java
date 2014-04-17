	import Utilities.Error;
	import Utilities.Visitor;
	import Utilities.Settings;
	import Utilities.SymbolTable;
	import Printers.*;
	import Scanner.*;
	import Parser.*;
	import AST.*;
	import java.io.*;
	import Library.*;
	
	public class ProcessJc {
		public static void usage() {
			System.out.println("ProcessJ Version 1.0");
			System.out.println("usage: pjc [-I dir] [-pp language] input");
			System.out.println("  -I dir\tSets the include directory (default is include)");
			System.out.println("  -pp\tDo not output code but produce a pretty print");
			System.out.println("     \tlanguage can be one of:");
			System.out.println("     \tlatex : produce latex includable output.");
			System.out.println("     \tprocessj : produce ProcessJ output.");
			System.out.println("  -sts\tDumps the global symbole table structure.");
			System.out.println("  -help\tPrints this message.");
		}
	
		static boolean sts = false;
	
	
		/*	public static void writeTree(Compilation c) {
			try {
				FileOutputStream fileOut = new FileOutputStream("Tree.ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(c);
				out.close();
				fileOut.close();
				System.out.printf("Serialized data is saved in Tree.ser");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 */
	
	
		public static void main(String argv[]) {
			AST root = null;
	
			if (argv.length == 0) {
				System.out.println("ProcessJ Compiler version 1.00");
				usage();
				System.exit(1);
			}
	
			int debugLevel = 0;
			for (int i = 0; i < argv.length; i++) {
				Scanner s = null;
				parser p = null;
				try {
					if ( argv[i].equals("-")) {
						s = new Scanner( System.in );
					} else if (argv[i].equals("-I")) {
						if (argv[i+1].charAt(argv[i+1].length()-1) == '/')
							argv[i+1] = argv[i+1].substring(0, argv[i+1].length()-1);
						Settings.includeDir = argv[i+1];
						i++;
						continue;
					} else if (argv[i].equals("-help")) {
						usage();
						System.exit(1);
						continue;
					} else if (argv[i].equals("-sts")) {
						sts = true;
						continue;
					} else {
						Error.setFileName(argv[i]);
						s = new Scanner( new java.io.FileReader(argv[i]) );
					}
					p = new parser(s);
				} catch (java.io.FileNotFoundException e) {
					System.out.println("File not found : \""+argv[i]+"\"");
					System.exit(1);
				} catch (ArrayIndexOutOfBoundsException e) {
					usage();
				}
	
				try {
					java_cup.runtime.Symbol r = ((parser)p).parse();
					root = (AST)r.value;
				} catch (java.io.IOException e) { e.printStackTrace(); System.exit(1);
				} catch (Exception e) { e.printStackTrace(); System.exit(1); }
	
				System.out.println("============= S = U = C = C = E = S = S =================");
	
				//((Compilation)root).visit(new PrettyPrinter<AST>());
				//writeTree((Compilation)root);
	
				Compilation c = (Compilation)root;
	
				// SYNTAX TREE PRINTER
				c.visit(new ParseTreePrinter());
	
	
				Library.decodePragmas(c);
				Library.generateLibraries(c);		
	
				Utilities.SymbolTable symtab;
	
				SymbolTable globalTypeTable = new SymbolTable("Main file: " + Error.fileName);
	
				////////////////////////////////////////////////////////////////////////////////
				// TOP LEVEL DECLARATIONS
				c.visit(new NameChecker.TopLevelDecls<AST>(globalTypeTable));
				globalTypeTable = SymbolTable.hook;
				
				if (sts)
					globalTypeTable.printStructure("");
	
				c.visit(new NameChecker.ResolvePackedTypes());
				
				////////////////////////////////////////////////////////////////////////////////
				// NAME CHECKER
				c.visit(new NameChecker.NameChecker<AST>(globalTypeTable));
				if (Error.errorCount != 0) {
					System.out.println("---------- Error Report ----------");
					System.out.println(Error.errorCount + " errors in symbol resolution - fix these before type checking.");
					System.out.println(Error.errors);
					System.exit(1);
				}
				
				//c.visit(new ParseTreePrinter());	

				// re-construct Array Types correctly
				root.visit(new NameChecker.ArrayTypeConstructor());
				System.out.println("ArrayConstructor ** DONE **");
				//try {
				c.visit(new TypeChecker.TypeChecker(globalTypeTable));
				/*		} catch (Exception e) {
					System.out.println("Oh no, something went wrong..." + e);
					if (e instanceof NullPointerException) {
						System.out.println("It was a null pointer exception");
						if (Error.errorCount != 0) {
							System.out.println("---------- Error Report ----------");
							System.out.println(Error.errorCount + " errors in symbol resolution - fix these before type checking.");
							System.out.println(Error.errors);
							System.exit(1);
						}
					}
				}*/
				// type checking
				// ... other checking
	
	
				// after last semantic checking is done ....
				// hack for now:
	
	
			}
	
		}
		public static void displayFile(String name) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(name));
				StringBuilder sb = new StringBuilder();
				String line;
				do {
					line = br.readLine();
					if (line != null)
						System.out.println(line);
				} while (line != null) ;
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	
	
