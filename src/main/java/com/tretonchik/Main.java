package com.tretonchik;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tretonchik.configuration.DatabaseConfiguration;
import com.tretonchik.configuration.JdbcDatabaseConfiguration;
import com.tretonchik.controller.*;
import com.tretonchik.json.deserializer.MemeDeserializer;
import com.tretonchik.json.deserializer.MemeReviewDeserializer;
import com.tretonchik.json.deserializer.UserDeserializer;
import com.tretonchik.json.deserializer.UserInteractionDeserializer;
import com.tretonchik.json.serializer.MemeReviewSerializer;
import com.tretonchik.json.serializer.MemeSerializer;
import com.tretonchik.json.serializer.UserInteractionSerializer;
import com.tretonchik.json.serializer.UserSerializer;
import com.tretonchik.models.*;
import com.tretonchik.service.*;
import io.javalin.Javalin;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.core.security.SecurityUtil.roles;
public class Main {
    public static UserRole getRole(Context context, Service<User, Integer> userService) {
        if (context.basicAuthCredentialsExist()) {
            String login = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            User actor = userService.findByColumnUnique("fname", login);
            if(login==null&&password==null){
                return UserRole.USER;
            }
            else if (BCrypt.checkpw(password, actor.getPassword())&&login!=null&&password!=null) {
                return actor.getRole();
            }
            else {
                throw new UnauthorizedResponse();
            }
        } else {
            return UserRole.USER;
        }
    }
    public static void main(String[] args) throws SQLException {
        DatabaseConfiguration configuration = new JdbcDatabaseConfiguration("jdbc:sqlite:C:\\Users\\77012\\Desktop\\бд\\dolbaniyeZnakomstva.db");
        Service<Meme, Integer> memeService=new MemeService(DaoManager.createDao(configuration.connectionSource(),Meme.class));
        Service<User, Integer> userService=new UserService(DaoManager.createDao(configuration.connectionSource(),User.class),
                DaoManager.createDao(configuration.connectionSource(),Meme.class),DaoManager.createDao(configuration.connectionSource(),MemeReview.class));
        Service<MemeReview, Integer> memeReviewService=new MemeReviewService(DaoManager.createDao(configuration.connectionSource(),MemeReview.class));
        Service<UserInteraction, Integer> userInteractionService=new UserInteractionService(DaoManager.createDao(configuration.connectionSource(),UserInteraction.class),
                DaoManager.createDao(configuration.connectionSource(),MemeReview.class),DaoManager.createDao(configuration.connectionSource(),User.class)
                );
        ReactionOperations reactionOperations=new ReactionOperations();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(
                        new SimpleModule()
                                .addDeserializer(User.class, new UserDeserializer())
                                .addDeserializer(Meme.class,new MemeDeserializer())
                                .addDeserializer(MemeReview.class,new MemeReviewDeserializer(DaoManager.createDao(configuration.connectionSource(),User.class)
                                ,DaoManager.createDao(configuration.connectionSource(),Meme.class),reactionOperations))
                                .addDeserializer(UserInteraction.class,new UserInteractionDeserializer(DaoManager.createDao(configuration.connectionSource(),
                                        User.class)))
                                .addSerializer(new UserSerializer())
                                .addSerializer(new MemeSerializer())
                                .addSerializer(new MemeReviewSerializer())
                                .addSerializer(new UserInteractionSerializer())
                );
//        Controller<User, Integer> userController = new UserController(userService, objectMapper,DaoManager.createDao(configuration.connectionSource(),Meme.class),
//                DaoManager.createDao(configuration.connectionSource(),User.class));
        Dao<User,Integer> userDao=DaoManager.createDao(configuration.connectionSource(),User.class);
        Controller<Meme, Integer> memeController = new MemeController(memeService, objectMapper,userDao,userService);
        MemeReviewController memeReviewController = new MemeReviewController(memeReviewService,objectMapper,userDao);
        UserInteractionController userInteractionController = new UserInteractionController(userInteractionService, objectMapper,DaoManager.createDao(configuration.connectionSource(),User.class),userService);
        UserController userController= new UserController(userService, objectMapper,DaoManager.createDao(configuration.connectionSource(),Meme.class),
               DaoManager.createDao(configuration.connectionSource(),User.class),reactionOperations);
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = "application/json";
            javalinConfig.prefer405over404 = true;
            javalinConfig.accessManager((handler, context, set) -> {
                Role userRole = getRole(context, userService);
                if (set.contains(userRole)) {
                    handler.handle(context);
                } else {
                    System.out.println("Here MAin");
                    throw new ForbiddenResponse();
                }
            });
        });
        app.routes(() -> {
            path("users", () -> {
                get(userController::getAll, roles(UserRole.ADMIN));
                post(userController::postOne,roles(UserRole.USER));
                path(":id", () -> {
                    get((ctx) -> userController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.USER, UserRole.ADMIN));
                    patch((ctx) -> userController.patchOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.USER, UserRole.ADMIN));
                    delete((ctx) -> userController.deleteOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(UserRole.USER, UserRole.ADMIN));
                });
            });
            path("meme", () -> {
                get(memeController::getAll, roles(UserRole.ADMIN));
                post(memeController::postOne, roles(UserRole.USER));
                path(":size", () -> {
                    get((ctx)->userController.UserMemeGetter(ctx),roles((UserRole.USER)));
                });
            });
            path("memeReview", () -> {
                get(memeReviewController::getAll, roles(UserRole.ADMIN));
                post(memeReviewController::postOne, roles(UserRole.USER));
                path(":memeId",()->{
                    path(":reaction",()->{
                        post((ctx)->userController.MemeReactionSetter(ctx),roles(UserRole.USER));
                    });
                });
            });
            path("userInteraction", () -> {
                get(userInteractionController::getAll, roles(UserRole.ADMIN));
                post(userInteractionController::postOne, roles(UserRole.USER));
                   path(":size",()->{
                       get((ctx)->userInteractionController.MatchesFinder(ctx),roles(UserRole.USER));
                   });
            });
        }).start(8080);
    }
}
