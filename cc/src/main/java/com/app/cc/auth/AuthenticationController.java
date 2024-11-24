package com.app.cc.auth;


import com.app.cc.Client.Client;
import com.app.cc.Createur.Createur;
import com.app.cc.file.file;
import com.app.cc.file.fileService;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
          @RequestParam(required = false) String nom,
          @RequestParam(required = false) String prenom,
          @RequestParam(required = false) String email,
          @RequestParam(required = false) String password,
          @RequestParam(required = false) Role role,
          @RequestParam(required = false) String lienInsta,
  @RequestParam(required = false)  String lienTikTok,
  @RequestParam(required = false)  String categoriesContenu,
          @RequestParam(required = false)  LocalDate dateNaissance,
          @RequestParam(required = false)   String nomEntreprise,
          @RequestParam(required = false)   String siteWebEntreprise,
          @RequestParam(required = false)  String secteurActivite ,
          @RequestParam(required = false)  Long numtel,
  @RequestParam(required = false) String bio,

          @RequestParam(required = false) MultipartFile image){
    try {

      file pdp = Fileservice.saveAttachment(image);
      RegisterRequest request = RegisterRequest.builder()
              .nom(nom)
              .prenom(prenom)
              .email(email)
              .bio(bio)
              .image(pdp)
              .dateNaissance(dateNaissance)
              .numtel(numtel)
              .lienInsta(lienInsta)
              .lienTikTok(lienTikTok)
              .siteWebEntreprise(siteWebEntreprise)
              .secteurActivite(secteurActivite)
              .nomEntreprise(nomEntreprise)
              .categoriesContenu(categoriesContenu)
              .password(password)
              .role(role)
              .build();
      registerresponse saved = serviceuser.register(request);
      return ResponseEntity.ok(saved);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }}


  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) throws Exception {

    return ResponseEntity.ok(serviceuser.authenticate(request));
  }
  @GetMapping("/find/{id}")
  public ResponseEntity<UserInfo> getUserById(@PathVariable("id") Long id){
      UserInfo user= serviceuser.findUserById2(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
  @GetMapping("/creators")
  public ResponseEntity<List<Createur>> getAllCreators() {
    List<Createur> creators = serviceuser.getAllCreators();
    if (creators.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(creators, HttpStatus.OK);
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
  public ResponseEntity<List<UserInfo>> getAllusers2() throws Exception {
    List<UserInfo> userr = serviceuser.findAllUsers1();
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
  public ResponseEntity<User> updateUser(
          @PathVariable Long id,
          @RequestParam(required = false) String nom,
          @RequestParam(required = false) String prenom,
          @RequestParam(required = false) String email,
          @RequestParam(required = false) String password,
          @RequestParam(required = false) Role role,
          @RequestParam(required = false) String lienInsta,
          @RequestParam(required = false)  String lienTikTok,
          @RequestParam(required = false)  String categoriesContenu,
          @RequestParam(required = false)  LocalDate dateNaissance,
          @RequestParam(required = false)   String nomEntreprise,
          @RequestParam(required = false)   String siteWebEntreprise,
          @RequestParam(required = false)  String secteurActivite ,
          @RequestParam(required = false)  Long numtel,
          @RequestParam(required = false) String bio,
          @RequestParam(required = false) MultipartFile image) {
    try {
      file pdp = null;
      if (image != null) {
        pdp =  Fileservice.saveAttachment(image);
      }
      RegisterRequest request = RegisterRequest.builder()
              .nom(nom)
              .prenom(prenom)
              .email(email)
              .bio(bio)
              .image(pdp)
              .dateNaissance(dateNaissance)
              .numtel(numtel)
              .lienInsta(lienInsta)
              .siteWebEntreprise(siteWebEntreprise)
              .lienTikTok(lienTikTok)
              .secteurActivite(secteurActivite)
              .nomEntreprise(nomEntreprise)
              .categoriesContenu(categoriesContenu)
              .password(password)
              .role(role)
              .password(password)
              .build();
      User saved = serviceuser.updateUser(id,request);
      return ResponseEntity.ok(saved);

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
  public ResponseEntity<UserInfo> getUserBymail2(@PathVariable("email") String email){
    UserInfo user = serviceuser.finduserByemail2(email);

    return new ResponseEntity<>(user, HttpStatus.OK);

  }

  }






