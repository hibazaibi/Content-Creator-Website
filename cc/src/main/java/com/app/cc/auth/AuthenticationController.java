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
                         @RequestParam(required = false)MultipartFile image,
                         @RequestParam(required = false) String nom,
  @RequestParam(required = false)String prenom,
  @RequestParam(required = false) String email,

  @RequestParam(required = false) String sexe,
  @RequestParam(required = false) String numtel,

  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
                                           String dateNaissance ,
  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String dateRecrutement,
  @RequestParam(required = false) String situationFam,
  @RequestParam(required = false) String nbEnfant,
  @RequestParam(required = false) String adresse,
  @RequestParam(required = false) String service,
  @RequestParam(required = false) String cnss,
  @RequestParam(required = false) String rib,
  @RequestParam(required = false)  String ncin,
  @RequestParam(required = false) String npassport,
  @RequestParam(required = false) String manager,
  @RequestParam(required = false) Role role ,
                         @RequestParam(required = false) String position
 ) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date datenaissance = null;
    if (dateNaissance != null) {
      try {
        datenaissance = dateFormat.parse(dateNaissance);
      } catch (ParseException e) {
        throw new RuntimeException("Failed to parse dateNaissance: " + dateNaissance, e);
      }
    }

    Date daterecrutement = null;
    if (dateRecrutement != null) {
      try {
        daterecrutement = dateFormat.parse(dateRecrutement);
      } catch (ParseException e) {
        throw new RuntimeException("Failed to parse dateRecrutement: " + dateRecrutement, e);
      }
    }
      User user1= serviceuser.finduserById(id);
    try { file attachement1=null;
      if (image!=null){
        attachement1=Fileservice.saveAttachment(image);
      }
      RegisterRequest request= RegisterRequest.builder()
              .nom(nom)
              .prenom(prenom)
              .email(email)

              .role(role)
              .build();
    User response= serviceuser.updateUser(id,request);
    return ResponseEntity.ok(response);
  } catch (Exception e) {
      throw new RuntimeException(e);
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






