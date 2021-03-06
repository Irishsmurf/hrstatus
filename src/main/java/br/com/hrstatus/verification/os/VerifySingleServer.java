/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.verification.os;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.com.hrstatus.action.os.unix.ExecRemoteCommand;
import br.com.hrstatus.verification.helper.VerificationHelper;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.hrstatus.action.os.windows.ExecCommand;
import br.com.hrstatus.controller.HomeController;
import br.com.hrstatus.model.Servidores;
import br.com.hrstatus.security.Crypto;

import com.jcraft.jsch.JSchException;

/*
 * @author spolti
 */

@Resource
public class VerifySingleServer extends VerificationHelper {

	protected final Logger log = Logger.getLogger(getClass().getName());
	
	@Autowired
	private Result result;

	
	@Get("/singleServerToVerify/{id}")
	public void singleServerToVerify(int id) throws JSchException, IOException {
		
		// Inserting HTML title in the result
		result.include("title", "Home");
		result.include("loggedUser", userInfo.getLoggedUsername());
		log.info("[ " + userInfo.getLoggedUsername() + " ] URI Called: /singleServerToVerify/" + id);
		Servidores servidor = this.serversDAO.getServerByID(id);

		if (!servidor.getSO().equals("WINDOWS")) {

			try {
				servidor.setPass(String.valueOf(Crypto.decode(servidor.getPass())));
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}
		}

		runSingleVerification(servidor);

		List<Servidores> list = this.serversDAO.listServerByID(id);
		result.include("class", "activeServer");
		result.include("server", list).forwardTo(HomeController.class).home("");
	}
	
	@SuppressWarnings("static-access")
	public void runSingleVerification(Servidores servidores) throws JSchException, IOException{
				
		if (servidores.getSO().equals("UNIX")) {
			servidores.setServerTime(getTime());
			servidores.setLastCheck(servidores.getServerTime());
			
			try {
				String dateSTR = ExecRemoteCommand.exec(servidores.getUser(), servidores.getIp(),servidores.getPass(), servidores.getPort(), "/bin/date");
				log.fine("[ " + userInfo.getLoggedUsername() + " ] Time retrieved from server " + servidores.getHostname() + ": " + dateSTR);
				servidores.setClientTime(dateSTR);
				// Calculating time difference
				servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));
				
				if (servidores.getDifference() < 0){
					servidores.setDifference(servidores.getDifference() * -1);
				}
					
				if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
					servidores.setTrClass("success");
					servidores.setStatus("OK");
				} else {
					servidores.setTrClass("error");
					servidores.setStatus("não OK");
				}
				try {
					// Encrypting the password
					servidores.setPass(encodePass.encode(servidores.getPass()));

				} catch (Exception e1) {
					log.severe("Error: " + e1);
				}
				this.serversDAO.updateServer(servidores);

			} catch (JSchException e) {
				servidores.setStatus(e + "");
				servidores.setTrClass("error");
				try {
					// Encrypting the password
					servidores.setPass(encodePass.encode(servidores.getPass()));

				} catch (Exception e1) {
					log.severe ("Error: " + e1);
				}
				this.serversDAO.updateServer(servidores);
			} catch (IOException e) {
				servidores.setStatus(e + "");
				servidores.setTrClass("error");
				
				try {
					// Encrypting the password
					servidores.setPass(encodePass.encode(servidores.getPass()));

				} catch (Exception e1) {
					log.severe("Error: " + e1);
				}
				this.serversDAO.updateServer(servidores);
			}
		}
		
		if (servidores.getSO().equals("WINDOWS")) {
			servidores.setServerTime(getTime());
			servidores.setLastCheck(servidores.getServerTime());
			try {
				String dateSTR = ExecCommand.Exec(servidores.getIp(),"I");
				if (dateSTR == null || dateSTR == ""){
					log.fine("[ " + userInfo.getLoggedUsername() + " ] net time paratmereter -I returned null, trying the paratemeter -S");
					dateSTR = ExecCommand.Exec(servidores.getIp(),"S");
				}
				log.fine("[ " + userInfo.getLoggedUsername() + " ] Time retrieved from server " + servidores.getHostname() + ": " + dateSTR);
				servidores.setClientTime(dateSTR);
				// Calculating time difference
				servidores.setDifference(differenceTime(servidores.getServerTime(), dateSTR));
				if (servidores.getDifference() < 0){
					servidores.setDifference(servidores.getDifference() * -1);
				}
				if (servidores.getDifference() <= this.configurationDAO.getDiffirenceSecs()) {
					servidores.setTrClass("success");
					servidores.setStatus("OK");
				} else {
					servidores.setTrClass("error");
					servidores.setStatus("não OK");
				}

				this.serversDAO.updateServer(servidores);
			} catch (IOException e) {
				servidores.setStatus(e + "");
				servidores.setTrClass("error");
				this.serversDAO.updateServer(servidores);
			}
		}
	}	
}