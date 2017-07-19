package com.boot.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.boot.entity.Catalog;
import com.boot.repository.CatalogRepository;



@RestController
public class CatalogController {
	
	private static final String CATALOG = "catalog";
	
	private static final String LOGO = "logo";
	
	@Resource
	CatalogRepository catalogRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Value("${username.name1}")
	private String Username;
	
	@RequestMapping(value = "/vim/{id}/catalog", method = RequestMethod.GET)
	public String get(@PathVariable("id") Long id) throws ClientProtocolException, IOException{
		
			System.out.println("===============> Entry For Hero "+Username);
			
			jdbcTemplate.execute("CREATE TABLE customers(" +
	                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

			//CloseableHttpClient httpclient = HttpClients.createDefault();
		 	//HttpGet httpget = new HttpGet("http://localhost:2020/vim/2/catalog");
		 	//CloseableHttpResponse s = httpclient.execute(httpget);
		 	//System.out.println("===============>   "+s.getEntity().toString());
			Iterable<Catalog> itr = catalogRepository.findAll();
			//Catalog c1 = catalogRepository.findByName("Ningan");
			//c1.setName("Ninganagouda");
			//catalogRepository.save(c1);
			Iterator< Catalog> it = itr.iterator();
			Catalog c = null;
			while(it.hasNext())
			{
				c = it.next();
				System.out.println("-------------->  "+c.getId()+"\t"+c.getName()+"\t"+c.getDescription());
			}
			
			return c.getId()+"\t"+c.getName()+"\t"+c.getDescription();
	}
/*
	@RequestMapping(value = "/catalog/{id}", method = RequestMethod.GET)
	public ResponseEntity<Catalog> getCatalog(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkPermissions(catalog);
			return new ResponseEntity<>(catalog, HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnf", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vnf>> getVnf(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkPermissions(catalog);
			return new ResponseEntity<>(catalog.getVnf(), HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnfMinus", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Vnf>> getVnfMinus(@PathVariable("id") Long id) throws NFVException {
		utility.checkAdminPermissions();
		Catalog c = getCatalog(id).getBody();
		Iterable<Vnf> allVnfs = vnfController.get(c.getVim().getId()).getBody();
		Iterable<Vnf> cVnfs = getVnf(id).getBody();
		
		for (Iterator<Vnf> i = allVnfs.iterator(); i.hasNext();) {
			Vnf v1 = i.next();
			for (Iterator<Vnf> j = cVnfs.iterator(); j.hasNext();) {
				Vnf v2 = j.next();
				if (v1.getId() == v2.getId()) {
					i.remove();
					break;
				}
			}
		}
		return new ResponseEntity<>(allVnfs, HttpStatus.OK);
	}

	@RequestMapping(value = "/catalog/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<Iterable<User>> getUser(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				return new ResponseEntity<>(catalog.getUser(), HttpStatus.OK);
			else {
				if (utility.isTenantAdminUser(user)) {
					if (catalog.getTenant() == null || catalog.getTenant() == user.getTenant())
						return new ResponseEntity<>(userRepository.findByCatalogAndTenant(catalog, user.getTenant()), HttpStatus.OK);
					else
						throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
				} else
					throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
			}
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnfinstance", method = RequestMethod.GET)
	public ResponseEntity<Iterable<VnfInstance>> getVnfInstance(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			User user = utility.getUser();
			if (utility.isAdminUser(user))
				return new ResponseEntity<>(catalog.getVnfInstance(), HttpStatus.OK);
			else {
				if (utility.isTenantAdminUser(user)) {
					if (catalog.getTenant() == null || catalog.getTenant() == user.getTenant()) {
						return new ResponseEntity<>(vnfInstanceRepository.findByCatalogAndTenant(catalog, user.getTenant()), HttpStatus.OK);
					} else
						throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
				} else {
					if (catalog.getUser().contains(user)) {
						return new ResponseEntity<>(vnfInstanceRepository.findByCatalogAndTenantAndUser(catalog, user.getTenant(), user), HttpStatus.OK);
					} else
						throw new NFVException(messageSource.getMessage("user.unauthorized", null, utility.getLocale()), HttpStatus.UNAUTHORIZED);
				}
			}
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/logo", produces = MediaType.ALL_VALUE, method = RequestMethod.GET)
	public ResponseEntity<byte[]> getLogo(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkPermissions(catalog);
			CatalogLogo logo = catalog.getCatalogLogo();
			if (logo != null) {
				return new ResponseEntity<>(logo.getLogo(), HttpStatus.OK);
			}
			throw new NFVException(messageSource.getMessage("not.found", new String[]{LOGO}, utility.getLocale()), HttpStatus.BAD_REQUEST);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog", method = RequestMethod.POST)
	public ResponseEntity<Catalog> create(@RequestBody Catalog catalog) throws NFVException {
		utility.checkAdminPermissions();
		return new ResponseEntity<>(catalogRepository.save(catalog), HttpStatus.OK);
	}

	@RequestMapping(value = "/catalog/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody Catalog c) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			catalog.setName(c.getName());
			catalog.setDescription(c.getDescription());
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnf", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateVnf(@PathVariable("id") Long id, @RequestBody Set<Vnf> vnf) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			catalog.setVnf(vnf);
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/vnfAdd", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateVnfAdd(@PathVariable("id") Long id, @RequestBody Set<Vnf> vnf) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			vnf.addAll(catalog.getVnf());
			catalog.setVnf(vnf);
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/user", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody Set<User> user) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			User loggedInUser = utility.getUser();
			Set<VnfInstance> list;
			if (utility.isAdminUser(loggedInUser))
				list = vnfInstanceRepository.findByCatalog(catalog);
			else
				list = vnfInstanceRepository.findByCatalogAndTenant(catalog, loggedInUser.getTenant());
			
			for (VnfInstance vnfInstance: list) {
				Set<User> users = vnfInstance.getUser();
				for (Iterator<User> i = users.iterator(); i.hasNext(); ) {
					User u = i.next();
					if (utility.isTenantAdminUser(loggedInUser) && u.getTenant() != loggedInUser.getTenant())
						continue;
					boolean found = false;
					for (User v: user) {
						if (u.getId() == v.getId()) {
							found = true;
							break;
						}
					}
					if (!found) {
						i.remove();
					}
				}
				vnfInstance.setUser(users);
				vnfInstanceRepository.save(vnfInstance);
			}
			
			if (utility.isAdminUser(loggedInUser))
				catalog.setUser(user);
			else {
				Set<User> users = userRepository.findByCatalogAndTenantIsNot(catalog, loggedInUser.getTenant());
				users.addAll(user);
				catalog.setUser(users);
			}
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}/logo", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateLogo(@PathVariable("id") Long id, @RequestParam(value = "icon", required = true) MultipartFile icon) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			CatalogLogo catalogLogo = catalog.getCatalogLogo();
			if (null == catalogLogo)
				catalogLogo = new CatalogLogo();
			try {
				catalogLogo.setLogo(icon.getBytes());
			} catch (IOException e) {
				throw new NFVException(messageSource.getMessage("unexpected.error", null, utility.getLocale()), HttpStatus.BAD_REQUEST);
			}
			catalogLogoRepository.save(catalogLogo);
			catalog.setCatalogLogo(catalogLogo);
			catalogRepository.save(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/catalog/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NFVException {
		Catalog catalog = catalogRepository.findOne(id);
		if (catalog != null) {
			utility.checkAdminPermissions();
			utility.checkPermissions(catalog);
			catalogRepository.delete(catalog);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new NFVException(messageSource.getMessage("not.found", new String[]{CATALOG}, utility.getLocale()), HttpStatus.BAD_REQUEST);
	}*/
	
	public void display()
	{
		//Added by tejesh
	}
	
}
