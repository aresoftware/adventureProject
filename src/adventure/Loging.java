package adventure;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.CaptureEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.ideario.bean.Consultas;
import com.ideario.vo.Usuario;

import adventure.servicios.UserWizard;


@ViewScoped
public class Loging {

	private MenuModel model;	
	private String nombre;
	private String clave;
	private Usuario usu = new Usuario();
	private UserWizard usuWizard = new UserWizard();
	private Servicios servicios;
	private ScheduleJava8View scheduleJava8View;
	
	public ScheduleJava8View getScheduleJava8View() {
		return scheduleJava8View;
	}
	public void setScheduleJava8View(ScheduleJava8View scheduleJava8View) {
		this.scheduleJava8View = scheduleJava8View;
	}
	public UserWizard getUsuWizard() {
		return usuWizard;
	}
	public void setUsuWizard(UserWizard usuWizard) {
		this.usuWizard = usuWizard;
	}
	public void setModel(MenuModel model) {
		this.model = model;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Usuario getUsu() {
		return usu;
	}
	public void setUsu(Usuario usu) {
		this.usu = usu;
	}
	public Servicios getServicios() {
		if (servicios==null)
			servicios = new Servicios();
		return servicios;
	}
	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String ingresar() {
		String s = "";
		Consultas c = new Consultas();
		usu = c.getIngreso(nombre, clave); 
		if (usu.isExiste()){
			if (Integer.valueOf(usu.getIdrol())==2) { //abogado
				scheduleJava8View = new ScheduleJava8View();
				scheduleJava8View.cargarAgendaAbogado(usu);
				usu.setEntrevistado(false);
				usu.setEntrevistador(true);
				s = "home";
			}else {//entrevistado
				scheduleJava8View = new ScheduleJava8View();
				scheduleJava8View.cargarAgendaEntrevistado(usu);
				usu.setEntrevistado(true);
				usu.setEntrevistador(false);
				s= "entrevistadoHome";
			}
			}
		else{
			addMessage("Failed", "Usuario o Password Errado");
			s = "";
			}
		
		
		return s;
	}
	
	public String creartabla() {
		
		Consultas c = new Consultas();
		String s = c.crearTablas();
		addMessage("Succes!!", s);
		return "";
	}
		
	public String registrar() {
		return "enrollUser";
	}
	
	
	public String casa() {
		return "home";
	}
	
	public String abrirCita() {
    	return "entrevista";
    	
    }
		
		 
	    @PostConstruct
	    public void init() {
	        model = new DefaultMenuModel();
	 
	        //First submenu
	        DefaultSubMenu firstSubmenu = DefaultSubMenu.builder()
	                .label("Dynamic Submenu")
	                .build();
	 
	        DefaultMenuItem item = DefaultMenuItem.builder()
	                .value("External")
	                .url("http://www.primefaces.org")
	                .icon("pi pi-home")
	                .build();
	        firstSubmenu.getElements().add(item);
	 
	        model.getElements().add(firstSubmenu);
	 
	        //Second submenu
	        DefaultSubMenu secondSubmenu = DefaultSubMenu.builder()
	                .label("Dynamic Actions")
	                .build();
	 
	        item = DefaultMenuItem.builder()
	                .value("Save")
	                .icon("pi pi-save")
	                .command("#{menuView.save}")
	                .update("messages")
	                .build();
	        secondSubmenu.getElements().add(item);
	 
	        item = DefaultMenuItem.builder()
	                .value("Delete")
	                .icon("pi pi-times")
	                .command("#{menuView.delete}")
	                .ajax(false)
	                .build();
	        secondSubmenu.getElements().add(item);
	 
	        item = DefaultMenuItem.builder()
	                .value("Redirect")
	                .icon("pi pi-search")
	                .command("#{menuView.redirect}")
	                .build();
	        secondSubmenu.getElements().add(item);
	 
	        model.getElements().add(secondSubmenu);
	    }
	 
	    public MenuModel getModel() {
	        return model;
	    }
	 
	    public void save() {
	        addMessage("Success", "Data saved");
	    }
	 
	    public void update() {
	        addMessage("Success", "Data updated");
	    }
	 
	    public void delete() {
	        addMessage("Success", "Data deleted");
	    }
	 
	    public void addMessage(String summary, String detail) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	        FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	    
	    
	    private String filename;
	     
	    private String getRandomImageName() {
	        int i = (int) (Math.random() * 10000000);
	         
	        return String.valueOf(i);
	    }
	 
	    public String getFilename() {
	        return filename;
	    }
	     
	    public void oncapture(CaptureEvent captureEvent) {
	        filename = getRandomImageName();
	        byte[] data = captureEvent.getData();
	 
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" +File.separator + "images" + File.separator + "photocam" + File.separator + filename + ".jpeg";
	        //String newFileName = externalContext.getRealPath("") + File.separator + filename + ".jpeg"; 
	        FileImageOutputStream imageOutput;
	        try {
	            imageOutput = new FileImageOutputStream(new File(newFileName));
	            imageOutput.write(data, 0, data.length);
	            imageOutput.close();
	        }
	        catch(IOException e) {
	        	addMessage("Camera", "Error Data deleted");
	        	
	            throw new FacesException("Error in writing captured image.", e);
	        }
	    }
		
}
