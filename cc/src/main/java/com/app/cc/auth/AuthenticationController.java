package com.app.cc.auth;


import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.file.fileService;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService serviceuser;
  private final UserRepository repo;
  private final fileService Fileservice;

  @PostMapping("/register")
  public ResponseEntity<registerresponse> register(
          @RequestBody RegisterRequest request
  ) throws Exception {
    return ResponseEntity.ok(serviceuser.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) throws Exception {

    return ResponseEntity.ok(serviceuser.authenticate(request));
  }
  @GetMapping("/find/{id}")
  public ResponseEntity<userinfo> getUserById(@PathVariable("id") Long id){
      userinfo user= serviceuser.finduserById2(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
  @GetMapping("/findclient/{id}")
  public ResponseEntity<Client> getClientById(@PathVariable("id") Long id){
    Client client= serviceuser.findclientById(id);
    return new ResponseEntity<>(client, HttpStatus.OK);
  }
  @GetMapping("/findcreateur/{id}")
  public ResponseEntity<Createur> getCreateurById(@PathVariable("id") Long id){
    Createur createur= serviceuser.findcreateurById(id);
    return new ResponseEntity<>(createur, HttpStatus.OK);
  }
  @GetMapping("/find2/{id}")
  public ResponseEntity<User> getUserById2(@PathVariable("id") Long id){
    User user= serviceuser.finduserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
  @PostMapping("/changepass")
  public ResponseEntity<changepasswordresponse> changePassword(
          @RequestBody changepasswordrequest request) throws Exception {
    return ResponseEntity.ok(serviceuser.changePassword(request));
  }

  @PostMapping("/forgetpass")
  public ResponseEntity<forgetpassresponse> forgetPassword(
          @RequestBody forgetpassrequest request
  ) throws Exception {

    return ResponseEntity.ok(serviceuser.forgetPassword(request));
  }

  @GetMapping("/users")


  public ResponseEntity<List<User>> getAllusers() {
    List<User> userr = serviceuser.findAllUsers();
    return new ResponseEntity<>(userr, HttpStatus.OK);
  }
  @GetMapping("/users2")
  public ResponseEntity<List<userinfo>> getAllusers2() throws Exception {
    List<userinfo> userr = serviceuser.findAllUsers1();
    return new ResponseEntity<>(userr, HttpStatus.OK);
  }
  @PostMapping("/forgetpass2")
  public ResponseEntity<forgetpass2response> forgetpass2(
          @RequestBody fogetpass2request request) throws Exception {
    return ResponseEntity.ok(serviceuser.forgetpass2(request));
  }
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws Exception {
    serviceuser.deleteUserAndTokensById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
  @PutMapping("/update/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody RegisterRequest registerreq) {
    try {
      User updatedUser = serviceuser.updateUser(id, registerreq);
      return ResponseEntity.ok(updatedUser);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(null);
    }
  }
    @GetMapping("/findbymail/{email}")
  public ResponseEntity<User> getUserBymail(@PathVariable("email") String email){
    User user = serviceuser.finduserByemail(email);

      return new ResponseEntity<>(user, HttpStatus.OK);

  }
  @GetMapping("/findbymail2/{email}")
  public ResponseEntity<userinfo> getUserBymail2(@PathVariable("email") String email){
    userinfo user = serviceuser.finduserByemail2(email);

    return new ResponseEntity<>(user, HttpStatus.OK);

  }

  }






