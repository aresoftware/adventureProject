package adventure;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.ideario.bean.Consultas;
import com.ideario.vo.Usuario;

import adventure.servicios.UserWizard;
import adventure.servicios.vo.EstadoParam;
import javax.servlet.http.Part;

@SessionScoped
public class Loging {

	private MenuModel model;	
	private String nombre;
	private String clave;
	private Usuario usu = new Usuario();
	private UserWizard usuWizard = new UserWizard();
	private Servicios servicios;
	private ScheduleJava8View scheduleJava8View;
	
	private EstadoParam estado;
	private List<EstadoParam> estados;
	private HtmlDataTable htmlDataTable;
	
	private File file;
	private Part uploadedFile;
	
    
	public Part getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	public EstadoParam getEstado() {
		return estado;
	}
	public void setEstado(EstadoParam estado) {
		this.estado = estado;
	}
	public List<EstadoParam> getEstados() {
		return estados;
	}
	public void setEstados(List<EstadoParam> estados) {
		this.estados = estados;
	}
	public HtmlDataTable getHtmlDataTable() {
		return htmlDataTable;
	}
	public void setHtmlDataTable(HtmlDataTable htmlDataTable) {
		this.htmlDataTable = htmlDataTable;
	}
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
		System.out.println("llama a ingresar "+nombre);
		String s = "";
		Consultas c = new Consultas();
		usu = c.getIngreso(nombre, clave); 
		//usu.setInformix(false);
		usu.setInformix(true);
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
			System.out.println("ingresar encontro "+nombre);
			}
		else{
			System.out.println("no pudo ingresar "+nombre);
			
			addMessage("Failed", "Usuario o Password Errado");
			s = "";
			}
		
		
		return s;
	}
	
	public String creartabla() {
		
		/*Consultas c = new Consultas();
		String s = c.crearTablas();
		addMessage("Succes!!", s);
		*/
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
	    
	    public String mostrarPaso() {
	    	Consultas c = new Consultas();
	    	this.estados = c.cambiarEstado(this.scheduleJava8View.getCita().getProceso());
	    	
	    	return "estado";
	    }
	    
	    
	    public String guardarPaso(){
	    	this.scheduleJava8View.getCita().getProceso().guardarAudiencia(this.estado.getCodigo());
	    	
	    	return "";
	    }
	    
	    public String mostrarPasoSel() {
	    	this.estado = (EstadoParam)htmlDataTable.getRowData();
    	
	    	if (estado != null)
	    		System.out.println(estado.getDesc());
	    	else
	    		System.out.println("navegue pues");
	    	
	    	return "entrevista";
    	}
	    
	    public void upload() {
	    	System.out.println("Successful entro"+this.estado.getDesc());
	        if (this.file != null) {
	        	System.out.println("Successful"+ this.estado.getFile().getFileName());
	        	
	            FacesMessage message = new FacesMessage("Successful", this.estado.getFile().getFileName() + " is uploaded.");
	            FacesContext.getCurrentInstance().addMessage(null, message);
	        }
	    }
	    
	    public void fileUpload(FileUploadEvent event)  {
	    	System.out.println("carga archivo");
	    	
	        String path = FacesContext.getCurrentInstance().getExternalContext()
	                .getRealPath("/");
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
	        String name = fmt.format(new Date())
	                + event.getFile().getFileName().substring(
	                      event.getFile().getFileName().lastIndexOf('.'));
	        File file = new File(path + "catalogo_imagens/temporario/" + name);
	        
	        try {
				
	        InputStream is = event.getFile().getInputStream();
	        OutputStream out = new FileOutputStream(file);
	        byte buf[] = new byte[1024];
	        int len;
	        while ((len = is.read(buf)) > 0)
	            out.write(buf, 0, len);
	        is.close();
	        out.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
				// TODO: handle exception
			}
	    }
	    
	    public void fileUploadLis(ActionEvent event)  {
	    	System.out.println(event.getComponent().getId());
	    	
	    }
	    
	    public void saveFile() {
	        try (
	        	 
	        	InputStream input = uploadedFile.getInputStream()) {
	        	String sfileName = "carta.pdf";
	        	
	            String uploads="C:\\DAVID";
				Files.copy(input, new File(uploads, sfileName).toPath());
				this.setFile(new File(sfileName));
				copyInputStreamToFile(input, file);
	            
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	            // Show faces message?
	        }
	    }
		
	 // InputStream -> File
	    private static void copyInputStreamToFile(InputStream inputStream, File file) 
			throws IOException {

	        try (FileOutputStream outputStream = new FileOutputStream(file)) {

	            int read;
	            byte[] bytes = new byte[1024];

	            while ((read = inputStream.read(bytes)) != -1) {
	                outputStream.write(bytes, 0, read);
	            }

				// commons-io
	            //IOUtils.copy(inputStream, outputStream);

	        }

	    }
}
