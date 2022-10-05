package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {

	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private String username;
	private static List<String> apps;
	private static List<ClientHandler> connections = new ArrayList<>();
	private static boolean aplicatieSQLStart = false;
	private static boolean aplicatieJSONStart = false;
	private static boolean aplicatieListaStart=false;
	private BazeDeDate bazaDeDate;
	private LucruFisierJSON lucruFisierJSON;
	private Lista lista;

	public ClientHandler(Socket socket, List<ClientHandler> connections) throws IOException {
		this.socket = socket;
		this.connections = connections;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintWriter(socket.getOutputStream(), true);
		this.username = null;
		this.bazaDeDate = new BazeDeDate(socket);
		this.lucruFisierJSON = new LucruFisierJSON(socket);

	}

	@Override
	public void run() {
		while (!this.socket.isClosed()) {
			try {
				
				this.writer.println("Introduceti comanda: ");
				String command = reader.readLine();

				if ("exit".equals(command.strip())) {
					this.socket.close();
				}
				else if("aplicatii".equals(command.strip())){
					apps=new ArrayList<String>();
					apps.add("aplicatie SQL");
					apps.add("aplicatie JSON");
					apps.add("aplicatie LISTA");
					this.writer.println("Lista aplicatii: ");
					for(String app: apps){
						this.writer.println(app);

					}
					
				}
				else if("help".equals(command.strip())){
					this.writer.println("Aveti la dispozitie 3 aplicatii: aplicatie SQL,aplicatie JSON,aplicatie List");
					this.writer.println("aplicatii: pentru a putea vedea lista,introduceti aplicatii");
					this.writer.println("");
					this.writer.println("start: pentru a putea incepe o aplicatie");
					this.writer.println("");
					this.writer.println("status: pentru a vedea daca aplicatia este folosita sau nu");
					this.writer.println("");

				}

				else if("start".equals(command.strip())){
					this.writer.println("Selectati aplicatia: ");
					this.writer.println("1) Aplicatie SQL");
					this.writer.println("2) Aplicatie JSON");
					this.writer.println("3) Aplicatie Lista");
					this.writer.println("4) Inapoi");
					command=reader.readLine();
					if("1".equals(command.strip())){
						if (aplicatieSQLStart) {
							this.writer.println("Deja foloseste cineva aplicatie aceasta va rog asteptati pana la deconectare");
						} else {
							aplicatieSQLStart = true;
							while (aplicatieSQLStart) {
								this.writer.println("Introduceti comanda: ");
								String SQL = reader.readLine();
								if ("vizualizare".equals(SQL.strip())) {
									bazaDeDate.afisareTable();
								} else if ("adaugaBD".equals(SQL.strip())) {
									this.writer.println("Introduceti ID-ul");
									command = reader.readLine();
									int id = Integer.parseInt(command);
									this.writer.println("Introduceti Prenumele");
									command = reader.readLine();
									String prenume = command;
									this.writer.println("Introduceti Numele");
									command = reader.readLine();
									String nume = command;
									this.writer.println("Introduceti Varsta");
									command = reader.readLine();
									int varsta = Integer.parseInt(command);
									bazaDeDate.inserareTable(id, prenume, nume, varsta);
								} else if ("where".equals(SQL.strip())) {
									this.writer.println("Introduce-ti varsta");
									int varsta = Integer.parseInt(reader.readLine());
									bazaDeDate.afisareWhereTable(varsta);
									this.writer.println("Introduceti comanda: ");
								}else if("help".equals(SQL.strip())){
									this.writer.println("Comenzi din aplicatia SQL");
									this.writer.println("");
									this.writer.println("vizualizare: arata toate datele din baza de date");
									this.writer.println("adaugaBD: introduceti noi date in baza de date");
									this.writer.println("where: interogare unde se arata numai datele care au varsta mai mare decat introdusa");
									this.writer.println("stop: iesiti din aplicatia SQL");
								}
								else if ("stop".equals(SQL.strip())) {
									aplicatieSQLStart = false;
									this.writer.println("Ati iesit din aplicatia SQL ");
								}
								else {this.writer.println("Comanda incorecta, va rog consultati 'help' ");}


							}
						}
					}
					else if("2".equals(command.strip())){
						if (aplicatieJSONStart) {
							this.writer.println("Deja foloseste cineva aplicatie aceasta va rog asteptati pana la deconectare");
							this.writer.println("Introduceti comanda: ");
						} else {
							aplicatieJSONStart = true;
							while (aplicatieJSONStart) {
								this.writer.println("Introduceti comanda: ");
								String JSON = reader.readLine();
								if("vizualizare_fisier".equals(JSON.strip())){
									lucruFisierJSON.vizualizareFisierJSON();
								}
								else if("adaugare_fisier_vector".equals(JSON.strip())){
									lucruFisierJSON.adaugareFisierJSONinJSONArray();
								}
								else if("vizualizare_vector".equals(JSON.strip())){
									lucruFisierJSON.vizualizareArrayJSON();
								}
								else if("adaugare".equals(JSON.strip())){
									lucruFisierJSON.introducereDate();
								}
								else if("vizualizare_fisier".equals(JSON.strip())){
									lucruFisierJSON.vizualizareFisierJSON();
								}
								else if("scriere_fisier".equals(JSON.strip())){
									lucruFisierJSON.scriereFisier();
								}
								else if("help".equals(JSON.strip())){
									this.writer.println("Comenzi aplicatie JSON");
									this.writer.println("");
									this.writer.println("vizualizare_fisier: se arata datele din fisierul JSON");
									this.writer.println("adaugare_fisier_vector: datele din fisier sunt introduse in vector");
									this.writer.println("vizualizare_vector: sunt vazute datele din vector");
									this.writer.println("adaugare: sunt adaugate date in vector");
									this.writer.println("vizualizare_fisier: sunt aratate datele din fisier");
									this.writer.println("scriere_fisier: scriem datele din vector in fisier");
									this.writer.println("stop: iesim din aplicatie JSON");
								}
								else if("stop".equals(JSON.strip())){
									aplicatieJSONStart=false;
									this.writer.println("Ati iesit din aplicatia JSON");
								}
								else {this.writer.println("Comanda incorecta, va rog consultati 'help' ");}
							}
						}
					}
					else if("3".equals(command.strip())){
						if (aplicatieListaStart) {
							this.writer.println("Deja foloseste cineva aplicatie aceasta va rog asteptati pana la deconectare");
							this.writer.println("Introduceti comanda: ");
						} else {
							aplicatieListaStart = true;
							while (aplicatieJSONStart) {
								this.writer.println("Introduceti comanda: ");
								String LISTA = reader.readLine();
								if("vizualizare".equals(LISTA.strip())){
									lista.vizualizareLista();
								}
								else if("adauga".equals(LISTA.strip())){
									lista.adaugareList();
								}
								else if("stop".equals(LISTA.strip())){
									aplicatieListaStart=false;
									this.writer.println("Ati iesit cu succes din aplicatie");
								}
								else if("help".equals(LISTA.strip())){
									this.writer.println("Comenzi pentru aplicatia lista");
									this.writer.println("");
									this.writer.println("vizualizare: putem vedea elementele din lista");
									this.writer.println("adauga: adaugam un element in lista");
									this.writer.println("stop: iesim din aplicatia lista");
								}
								else {this.writer.println("Comanda incorecta, va rog consultati 'help' ");}

							}
						}
					} else if("4".equals(command.strip())){
						
					}
					else{this.writer.println("Va rog introduce-ti un numar din cele de mai sus");}
				}

				else if("status".equals(command.strip())){
					this.writer.println("Selectati aplicatia: ");
					this.writer.println("1) Aplicatie SQL");
					this.writer.println("2) Aplicatie JSON");
					this.writer.println("3) Aplicatie Lista");
					this.writer.println("4) Inapoi");
					command=reader.readLine();
					if("1".equals(command.strip())){
						this.writer.println("Aplicatie SQL este folosita: " + aplicatieSQLStart);
					}
					else if("2".equals(command.strip())){
						this.writer.println("Aplicatie JSON ruleaza: "+aplicatieJSONStart);
					}
					else if("3".equals(command.strip())){
						this.writer.println("Aplicatie Lista este folosita: "+aplicatieListaStart);
					}
					else if("4".equals(command.strip())){
					}
				}
					 else {
						this.writer.println("Comanda incorecta, introduceti 'help' pentru a vedea omenzile disponibile ");
					}
				} catch(Exception e){
					this.writer.println(e.getMessage());
					this.writer.flush();
				}
			}
		}
	}

