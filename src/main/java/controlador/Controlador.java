package controlador;



import Modelo.Jugador;
import Modelo.OpcionRespuesta;
import Modelo.Pregunta;
import Modelo.Preguntas;
import Modelo.RondadeJuego;
import Vista.Vista2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jhosepe11
 */
public class Controlador implements ActionListener{
    RondadeJuego newPlay;
    Vista2 objVista;
    String [] listPreguntas;
    int id=0;
    String [] categorias = {"ciencia","geografia","historia","deportes","arte"};
    boolean bandera = true;

    public Controlador() {
        this.newPlay = new RondadeJuego();
        this.objVista = new Vista2();
        objVista.getBtJugar().addActionListener(this);
        objVista.getBtAjustes().addActionListener(this);
        objVista.getBtSiguiente().addActionListener(this);
        
    }
    
    public void Iniciar(){
        
        objVista.setVisible(true);
        objVista.setLocationRelativeTo(null);
        this.ocultarContenido();
   
    }
    
    public void ocultarContenido(){
       objVista.getrBopcionA().setVisible(false);
       objVista.getrBopcionB().setVisible(false);
       objVista.getrBopcionC().setVisible(false);
       objVista.getrBopcionD().setVisible(false);
       objVista.getJlbNombrePlayer().setVisible(false);
    }
    
       public void mostrarContenido(){
       objVista.getrBopcionA().setVisible(true);
       objVista.getrBopcionB().setVisible(true);
       objVista.getrBopcionC().setVisible(true);
       objVista.getrBopcionD().setVisible(true);
       objVista.getBtSiguiente().setEnabled(true);
    }
    
  
    public void reguistroJugador(){
        String nombreJugador = JOptionPane.showInputDialog(objVista,"Ingrese su Nombre :","Nombre del Jugador",1);
        Jugador jp = new Jugador(id,nombreJugador);
        newPlay.setPlayer(jp);
        
        objVista.getJlbNombrePlayer().setText(nombreJugador);
        objVista.getJlbNombrePlayer().setVisible(true);
        id++;
    } 
    

    public String darFormato(String id,String nombre,String puntos){
        return id+";"+nombre+";"+puntos;
    }
     
    
    @Override
    public void actionPerformed(ActionEvent e) {
               
        if(e.getSource()==objVista.getBtJugar()){
            /*JOptionPane.showMessageDialog(objVista,"esta funcionando el boton jugar","mensaje",1);*/
            this.mostrarContenido();
            this.iniciarJuego();
            this.pintarPregunta(this.selecctorPregunta(newPlay.getNivelRonda()));
        }
        if(e.getSource()==objVista.getBtAjustes()){
            JOptionPane.showMessageDialog(objVista,"esta funcionando el boton ajustes","mensaje",1);
            

             
                                                                         
                
            }
            
        }
     public Pregunta selecctorPregunta(int ronda){
         Random aletorio = new Random();
         Preguntas pregCategori;
         Pregunta salidaP;
         List<Pregunta> preguntasS =null;
         
         int pregSelccion = aletorio.nextInt(5);
         pregCategori = newPlay.getPreguntasRonda().get(ronda);
         preguntasS = pregCategori.getPreguntas();
         salidaP = preguntasS.get(pregSelccion);
        
         return salidaP;
     }
    
     public void pintarPregunta(Pregunta nuevaPregunta){
         
         
         objVista.getjTextAreaPregunta().setText(nuevaPregunta.getTxtPrgunta());
         objVista.getrBopcionA().setText("A. "+nuevaPregunta.getListOpiones().get(0).getTextRespuesta());
         objVista.getrBopcionB().setText("B. "+nuevaPregunta.getListOpiones().get(1).getTextRespuesta());
         objVista.getrBopcionC().setText("C. "+nuevaPregunta.getListOpiones().get(2).getTextRespuesta());
         objVista.getrBopcionD().setText("D. "+nuevaPregunta.getListOpiones().get(3).getTextRespuesta());
         
     } 
     
    /*guardar Jugador*/
     public void guardarJugador(Jugador jp){
          String guardar = this.darFormato(""+jp.getiD(),jp.getNombreJugador(),""+jp.getPuntos());;
        /*JOptionPane.showConfirmDialog(null,guardar,"Notificacion",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);*/
          this.escribirArchivo(guardar);
     }
     
    /* Crearemos nuentro objeto juego*/
       /*Crear lista de opciones opciones*/
      public String[] separadorPregunta(String preguntaEntrada){
          String[] partesPregunta;
          partesPregunta = preguntaEntrada.split(";");
          return partesPregunta;
      }
       public List<OpcionRespuesta> crearOpciones(String preguntaEntrada){
           List<OpcionRespuesta> listSalida = new ArrayList<>();
           String[] partesPregunta;
           partesPregunta = preguntaEntrada.split(";");
           String respuesta = partesPregunta[5];
           int numCorrecta = 0;
              switch (respuesta) {
               case "a" -> numCorrecta = 0;
               case "b" -> numCorrecta = 1;
               case "c" -> numCorrecta = 2;
               case "d" -> numCorrecta = 3;
           }
            for (int i = 0; i < 4; i++) {
                
                String textOpc = partesPregunta[i];
                boolean esCorrecta = false;
                    if(i==numCorrecta){
                        esCorrecta = true;
                    }
                listSalida.add(new OpcionRespuesta(textOpc,esCorrecta));
           }         
            
            return listSalida;
       } 
       
       public Pregunta crearPregunta(List<OpcionRespuesta> opcRespuesta, String[] pregunta){
          
           Pregunta preguntaSalida;
           int nivelPreg = Integer.parseInt(pregunta[6]);
           String textPregungta = pregunta[0];
           
           preguntaSalida = new Pregunta(textPregungta,opcRespuesta,nivelPreg);
           
           return preguntaSalida;
       
       }
       
      
      
      public void iniciarJuego(){
          List<String> preguntas = this.leerArchivo();
          List<OpcionRespuesta> listOpcR= new ArrayList<>();
          Pregunta preG;
          List<Pregunta> listaPregunta = new ArrayList<>() ;
          Preguntas bancoPre;
          List<Preguntas> bancoPreguntas = new ArrayList<>() ;
          String categoria;
             for (int i = 0; i < preguntas.size(); i++) {
                  //JOptionPane.showMessageDialog(objVista,preguntas[i]+""+preguntas.length,"mensaje",1);
                  //JOptionPane.showMessageDialog(objVista,preguntas[i+1]+""+preguntas.length,"mensaje",1);
                 listOpcR = this.crearOpciones(preguntas.get(i));
                 preG = this.crearPregunta(listOpcR,this.separadorPregunta(preguntas.get(i)));
                 categoria = (this.separadorPregunta(preguntas.get(i)))[7];
                 listaPregunta.add(preG);
                    if(((i+1)%5)==0){
                        bancoPre = new Preguntas(listaPregunta, categoria);
                        bancoPreguntas.add(bancoPre);
                        listaPregunta.clear();
                    }
          }
        this.reguistroJugador();
        newPlay.setPreguntasRonda(bancoPreguntas);
        
          
      
      }
     
    
    
     
    FileWriter salida;
    PrintWriter escritor;
    FileReader entrada;
    File archivo=null;
    BufferedReader lector;
    
    public void escribirArchivo(String Dato){
        try {
            salida = new FileWriter("Historico.txt",true);
             escritor = new PrintWriter(salida);
             escritor.println(Dato);
             escritor.close();
             salida.close();
        } catch (IOException ex) {
            JOptionPane.showConfirmDialog(objVista,"Datos ingresados","Notificacion",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
        }
       
        
    }
    /*Programamos La lectura de Archivos*/
     public List<String> leerArchivo(){
         List<String>listPreguntasTemp = new ArrayList<String>(); 
         try {
             archivo = new  File("D:\\Jhosepe11\\Documents\\NetBeansProjects\\Challenge\\Preguntas.txt");
             entrada = new FileReader(archivo);
             lector =new BufferedReader(entrada);
             String linea;
           
             linea=lector.readLine();
              for (int j = 0; j < 25; j++) {
                  //JOptionPane.showMessageDialog(objVista,linea,"mensaje",1);
                 listPreguntasTemp.add(linea);
                 linea = lector.readLine();
             }
                
                 
             
            
         } catch (IOException e) {
             JOptionPane.showConfirmDialog(objVista,"El Archivo no se pudo cargar","Notificacion",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
         }
         /*JOptionPane.showMessageDialog(objVista,"esto es lo que hay  "+listPreguntasTemp[0],"mensaje",1);*/
         return listPreguntasTemp;
     }
    }

  
    
    

