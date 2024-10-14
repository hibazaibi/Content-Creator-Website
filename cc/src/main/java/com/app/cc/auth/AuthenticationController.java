package com.app.cc.auth;


import com.app.cc.file.file;
import com.app.cc.file.fileService;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
  ) {

    return ResponseEntity.ok(serviceuser.authenticate(request));
  }
  @GetMapping("/find/{id}")
  public ResponseEntity<userinfo> getUserById(@PathVariable("id") Long id){
      userinfo user= serviceuser.finduserById2(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
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
  public ResponseEntity<User> updateUser(@PathVariable Long id,
                                         @RequestParam(required = false) MultipartFile image,
                                         @RequestParam(required = false) String nom,
                                         @RequestParam(required = false) String prenom,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) Long numtel,
                                         @RequestParam(required = false) String nomEntreprise,
                                         @RequestParam(required = false) String siteWebEntreprise,
                                         @RequestParam(required = false) String secteurActivite,
                                         @RequestParam(required = false) String bio,
                                         @RequestParam(required = false) String lienInsta,
                                         @RequestParam(required = false) String lienTikTok,
                                         @RequestParam(required = false) String categoriesContenu,
                                         @RequestParam(required = false) Role role) {
    // Log incoming request
    System.out.println("Updating user with ID: " + id);
    System.out.println("Received data: nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", role=" + role);
    System.out.println("Client fields: nomEntreprise=" + nomEntreprise + ", siteWebEntreprise=" + siteWebEntreprise + ", secteurActivite=" + secteurActivite);
    System.out.println("Creator fields: bio=" + bio + ", lienInsta=" + lienInsta + ", lienTikTok=" + lienTikTok + ", categoriesContenu=" + categoriesContenu);
      try {
          RegisterRequest registerRequest = RegisterRequest.builder()
                  .nom(nom)
                  .prenom(prenom)
                  .email(email)
                  .numtel(numtel)
                  .nomEntreprise(nomEntreprise)
                  .siteWebEntreprise(siteWebEntreprise)
                  .secteurActivite(secteurActivite)
                  .bio(bio)
                  .lienInsta(lienInsta)
                  .lienTikTok(lienTikTok)
                  .categoriesContenu(categoriesContenu)
                  .role(role)
                  .build();

          if (image != null) {
              file savedImage = Fileservice.saveAttachment(image);
              registerRequest.setImage(savedImage);
          }

          User updatedUser = serviceuser.updateUser(id, registerRequest);

          return ResponseEntity.ok(updatedUser);

      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }}
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






