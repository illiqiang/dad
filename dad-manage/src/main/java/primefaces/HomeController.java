package primefaces;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "Home")
public class HomeController {
	public String sayHello() {
		return "hello JSF !";
	}
}
