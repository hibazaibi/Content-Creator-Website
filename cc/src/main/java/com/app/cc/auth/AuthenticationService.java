package com.app.cc.auth;

import com.app.cc.Client.Client;
import com.app.cc.Client.ClientRepository;
import com.app.cc.Createur.Createur;
import com.app.cc.Createur.CreateurRepository;
import com.app.cc.config.JwtService;
import com.app.cc.email.EmailSender;
import com.app.cc.exeption.UserNotFoundException;
import com.app.cc.file.fileService;
import com.app.cc.token.Token;
import com.app.cc.token.TokenRepository;
import com.app.cc.token.TokenType;
import com.app.cc.user.Role;
import com.app.cc.user.User;
import com.app.cc.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
private final ClientRepository clientRepository;
private final CreateurRepository createurRepository;

  private final fileService Fileservice;

  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

private String idimage;
  public registerresponse register(RegisterRequest request) throws Exception {
    User user;
    var existingUser = repository.findByEmail(request.getEmail());
    if (existingUser.isPresent()) {
      throw new Exception("User already exists with this email.");
    }
    if (request.getRole() == Role.CREATOR) {
      System.out.println("Creating Creator");
      user = Createur.builder()
              .nom(request.getNom())
              .prenom(request.getPrenom())
              .email(request.getEmail())
             .password(passwordEncoder.encode(request.getPassword()))
              .role(Role.CREATOR)
              .bio(request.getBio())
              .lienInsta(request.getLienInsta())
              .lienTikTok(request.getLienTikTok())
              .categoriesContenu(request.getCategoriesContenu())
              .active(false)
              .image(request.getImage())
              .build();
    } else if (request.getRole() == Role.CLIENT) {
      System.out.println("Creating Client");
      user = Client.builder()
              .nom(request.getNom())
              .prenom(request.getPrenom())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .role(Role.CLIENT)
              .nomEntreprise(request.getNomEntreprise())
              .siteWebEntreprise(request.getSiteWebEntreprise())
              .secteurActivite(request.getSecteurActivite())
              .image(request.getImage())
              .active(false)
              .build();
    } else if (request.getRole() == Role.ADMIN) {
      System.out.println("Creating General User");
      user = User.builder()
              .nom(request.getNom())
              .prenom(request.getPrenom())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .role(Role.ADMIN)
               .image(request.getImage())
              .active(true)
              .build();
    } else {
      throw new Exception("Invalid role provided.");
    }
    System.out.println("User to be saved: " + user);
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    saveUserToken(savedUser, jwtToken);
    return registerresponse.builder()
            .token(jwtToken)
            .nom(user.getNom())
            .email(user.getEmail())
            .prenom(user.getPrenom())
            .role(user.getRole())
            .build();
  }


  public void activateUser(Long userId) throws Exception {
    User user = repository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));

    user.setActive(true);
    repository.save(user);
  }


  public changepasswordresponse changePassword(changepasswordrequest request) throws Exception {
    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new Exception("User not found"));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new Exception("Invalid current password");
    }

    if (!isValidPassword(request.getNewPassword())) {
      throw new Exception("Invalid new password");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    repository.save(user);
    EmailSender emailSender = new EmailSender();
    String subject = "Modification du mot de passe";
    String body = "Bonjour " +user.getNom()+",<br>Nous vous informons que votre mot de passe a été modifié.<br>Si vous n'avez pas modifié votre mot de passe récemment, nous vous recommandons de prendre des mesures immédiates pour protéger votre compte.<br>Cordialement.";
    emailSender.sendEmail(user.getEmail(), subject, body);
    var msga = "salem";
    return changepasswordresponse.builder().msg(msga).email(user.getEmail()).currentPassword(request.getCurrentPassword()).newPassword(request.getNewPassword()).build();
  }
  public forgetpass2response forgetpass2(fogetpass2request request) throws Exception {
    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new Exception("User not found"));



    if (!isValidPassword(request.getNewpassword())) {
      throw new Exception("Invalid new password");
    }

    user.setPassword(passwordEncoder.encode(request.getNewpassword()));
    repository.save(user);
    EmailSender emailSender = new EmailSender();
    String subject = "Modification du mot de passe";
    String body = "Bonjour " +user.getNom()+",<br>Nous vous informons que votre mot de passe a été modifié.<br>Si vous n'avez pas modifié votre mot de passe récemment, nous vous recommandons de prendre des mesures immédiates pour protéger votre compte.<br>Cordialement.";
    emailSender.sendEmail(user.getEmail(), subject, body);
    var msga = "salem";
    return forgetpass2response.builder().msg(msga).email(user.getEmail()).newPassword(request.getNewpassword()).build();
  }
  private boolean isValidPassword(String newPassword) {
    return true;
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();
    if (!user.isActive()) {
      throw new Exception("Account not activated by admin.");
    }
    var jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .role(user.getRole())
            .token(jwtToken).email(user.getEmail())
            .id(user.getId())
            .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }
  public User finduserById(Long id){
    return  repository.findById(id).orElseThrow(()-> new UserNotFoundException("user by id"+id+"notfound"));
  }
  public Client findclientById(Long id){
    return  clientRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user by id"+id+"notfound"));
  }
  public Createur findcreateurById(Long id){
    return  createurRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user by id"+id+"notfound"));
  }
  public List<Createur> getAllCreators() {
    return createurRepository.findAllCreators();
  }


  public List<UserInfo> getAllCreators2() {
    List<Createur> users = createurRepository.findAllCreators();
    if (users.isEmpty()) {
      throw new UserNotFoundException("No users found.");
    }
    List<UserInfo> userinfos= new ArrayList<>();
    for (Createur user : users) {
      UserInfo userinfo = new UserInfo();
      userinfo.setId(user.getId());
      userinfo.setNom(user.getNom());
      userinfo.setPrenom(user.getPrenom());
      userinfo.setEmail(user.getEmail());
      userinfo.setLienInsta(user.getLienInsta());
      userinfo.setLienTikTok(user.getLienTikTok());
      userinfo.setCategoriesContenu(user.getCategoriesContenu());
      userinfo.setBio(user.getBio());
      userinfo.setPassword(user.getPassword());
      userinfo.setRole(user.getRole());
      if(user.getNumberOfRatings()==0)
      {
        userinfo.setAvgrattings(0.0);
      }else{
      userinfo.setAvgrattings((double) (user.getTotalRatings()/user.getNumberOfRatings()));}
      if (user.getImage()!=null){
        userinfo.setImage(user.getImage());

      }

      userinfos.add(userinfo);
    }

    return userinfos;
  }

  public forgetpassresponse forgetPassword(forgetpassrequest request) throws Exception {
    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    var jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);


    String resetPasswordLink = "http://localhost:4200/mdpoublie?token="+jwtToken ;


    EmailSender emailSender = new EmailSender();
    String subject = "Réinitialiser votre mot de passe";
    String body = "Bonjour " + user.getNom() +",<br><br>Nous avons reçu une demande de réinitialisation de mot de passe pour votre compte.Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet e-mail.<br><br>Cliquez sur le bouton ci-dessous pour réinitialiser votre mot de passe:<br><br><a href=" + resetPasswordLink + "><button style=\"background-color:#008CBA;color:white;padding:12px 20px;border:none;border-radius:4px;\">Réinitialiser le mot de passe</button></a><br><br>Cordialement";
    emailSender.sendEmail(user.getEmail(), subject, body);
    var msg1 = "gggg";
    return forgetpassresponse.builder().msg(msg1).build();
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public List<User> findAllUsers() {

    return repository.findAll();
  }

  public List<UserInfo> findAllUsers1() throws Exception {
    List<User> users = repository.findAll();
    if (users.isEmpty()) {
      throw new UserNotFoundException("No users found.");
    }
    List<UserInfo> userinfos= new ArrayList<>();
    for (User user : users) {
      UserInfo userinfo = new UserInfo();
      userinfo.setId(user.getId());
      userinfo.setNom(user.getNom());
      userinfo.setPrenom(user.getPrenom());
      userinfo.setEmail(user.getEmail());

      userinfo.setPassword(user.getPassword());
      userinfo.setRole(user.getRole());


        userinfos.add(userinfo);
    }

    return userinfos;
  }
  @Transactional
  public void deleteUserAndTokensById(Long id) throws Exception {
    Optional<User> userOptional = repository.findById(id);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      List<Token> tokens = tokenRepository.findByUser(user);
      tokenRepository.deleteAll(tokens);
      repository.save(user);
      repository.delete(user);
    } else {
      throw new UserNotFoundException("User not found with ID: " + id);
    }
}
  public User updateUser(Long id, RegisterRequest newUser) throws Exception {
    User user = repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    if (newUser.getImage() != null && user.getImage() != null) {
      this.idimage = user.getImage().getId();
    }
    if (newUser.getNom() != null) {
      user.setNom(newUser.getNom());
      System.out.println("Updated nom to: " + newUser.getNom());
    }
    System.out.println(user.getClass().getName());
    if (newUser.getPrenom() != null) {
      user.setPrenom(newUser.getPrenom());
      System.out.println("Updated prenom to: " + newUser.getPrenom());
    }
    if (newUser.getEmail() != null) {
      user.setEmail(newUser.getEmail());
      System.out.println("Updated email to: " + newUser.getEmail());
    }

    if (newUser.getNumtel() != null) {
      user.setNumtel(newUser.getNumtel());
      System.out.println("Updated numtel to: " + newUser.getNumtel());
    }
    if (newUser.getDateNaissance() != null) {
      user.setDateNaissance(newUser.getDateNaissance());
      System.out.println("Updated datenaiss to: " + newUser.getDateNaissance());
    }if (user instanceof Client) {
      Client client = (Client) user;
      if (newUser.getNomEntreprise() != null) {
        client.setNomEntreprise(newUser.getNomEntreprise());
        System.out.println("Updated nomEntreprise to: " + newUser.getNomEntreprise());
      }
      if (newUser.getSiteWebEntreprise() != null) {
        client.setSiteWebEntreprise(newUser.getSiteWebEntreprise());
        System.out.println("Updated siteWebEntreprise to: " + newUser.getSiteWebEntreprise());
      }
      if (newUser.getSecteurActivite() != null) {
        client.setSecteurActivite(newUser.getSecteurActivite());
        System.out.println("Updated secteurActivite to: " + newUser.getSecteurActivite());
      }
    } else if (user instanceof Createur) {
      Createur createur = (Createur) user;
      if (newUser.getBio() != null) {
        createur.setBio(newUser.getBio());
        System.out.println("Updated bio to: " + newUser.getBio());
      }
      if (newUser.getLienInsta() != null) {
        createur.setLienInsta(newUser.getLienInsta());
        System.out.println("Updated lienInsta to: " + newUser.getLienInsta());
      }
      if (newUser.getLienTikTok() != null) {
        createur.setLienTikTok(newUser.getLienTikTok());
        System.out.println("Updated lienTikTok to: " + newUser.getLienTikTok());
      }
      if (newUser.getCategoriesContenu() != null) {
        createur.setCategoriesContenu(newUser.getCategoriesContenu());
        System.out.println("Updated categoriesContenu to: " + newUser.getCategoriesContenu());
      }
    }
    if (newUser.getImage() != null) {
      user.setImage(newUser.getImage());
    }
    User savedUser = repository.save(user);
    System.out.println("Saved user: " + savedUser);
    if (newUser.getImage()!=null&&this.idimage!=null){
      Fileservice.deleteAttachment(this.idimage);
    }
    return savedUser;
  }

  public UserInfo findUserById2(Long id) {
    User user = repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

    UserInfo userInfo = new UserInfo();
    userInfo.setId(user.getId());
    userInfo.setNom(user.getNom());
    userInfo.setPrenom(user.getPrenom());
    userInfo.setEmail(user.getEmail());
    userInfo.setPassword(user.getPassword());
    userInfo.setRole(user.getRole());
    userInfo.setNumtel(user.getNumtel());
userInfo.setDateNaissance(user.getDateNaissance());
    if (user.getImage()!=null){
      userInfo.setImage(user.getImage());

    }


    if (user instanceof Createur) {
      Createur creator = (Createur) user;
      userInfo.setLienInsta(creator.getLienInsta());
      userInfo.setLienTikTok(creator.getLienTikTok());
      userInfo.setCategoriesContenu(creator.getCategoriesContenu());
      userInfo.setBio(creator.getBio());
    } else if (user instanceof Client) {
      Client client = (Client) user;
      userInfo.setNomEntreprise(client.getNomEntreprise());
      userInfo.setSiteWebEntreprise(client.getSiteWebEntreprise());
      userInfo.setSecteurActivite(client.getSecteurActivite());
    }

    return userInfo;
  }


  public User finduserByemail(String email){
    return  repository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("user by id"+email+"notfound"));


  }
  public UserInfo finduserByemail2(String email){
   User user1=  repository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("user by id"+email+"notfound"));
    UserInfo user = new UserInfo();
    user.setNom(user1.getNom());
    user.setPrenom(user1.getPrenom());
    user.setEmail(user1.getEmail());
    user.setPassword(user1.getPassword());
    user.setNumtel(user.getNumtel());
    user.setDateNaissance(user.getDateNaissance());
    user.setRole(user1.getRole());

    return user;
  }
  public long countAllUsers() {
    return repository.count();
  }

  public long countClients() {
    return repository.countByRole(Role.CLIENT);
  }

  public long countCreators() {
    return repository.countByRole(Role.CREATOR);
  }
  public Map<String, Long> getUserActivityCounts() {
    Long activeUsers = repository.countActiveUsers();
    Long inactiveUsers = repository.countInactiveUsers();

    Map<String, Long> activityCounts = new HashMap<>();
    activityCounts.put("Active", activeUsers);
    activityCounts.put("Inactive", inactiveUsers);

    return activityCounts;
  }

  }
