package com.digiSchool.digiSchool.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.Exceptionconfig.model.Arrondissement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Departement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.Region;
import com.digiSchool.digiSchool.Exceptionconfig.model.Ville;
import com.digiSchool.digiSchool.Exceptionconfig.repository.ArrondissementRepository;
import com.digiSchool.digiSchool.Exceptionconfig.repository.DepartementRepository;
import com.digiSchool.digiSchool.Exceptionconfig.repository.QuartierRepository;
import com.digiSchool.digiSchool.Exceptionconfig.repository.VilleRepository;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.Niveau;
import com.digiSchool.digiSchool.academic.organisation.model.SousSysteme;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.RegionRepository;
import com.digiSchool.digiSchool.user.model.Role;
import com.digiSchool.digiSchool.user.model.Utilisateur;
import com.digiSchool.digiSchool.user.repository.RoleRepository;
import com.digiSchool.digiSchool.user.repository.UtilisateurRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final DepartementRepository departementRepository;
    private final ArrondissementRepository arrondissementRepository;
    private final VilleRepository villeRepository;
    private final QuartierRepository quartierRepository;
    private final EcoleRepository ecoleRepository;
    private final AnneescolaireRepository anneescolaireRepository;
    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ClasseRepository classeRepository;

    private static final String TENANT = "default";

    public DataSeeder(RegionRepository regionRepository,
                      DepartementRepository departementRepository,
                      ArrondissementRepository arrondissementRepository,
                      VilleRepository villeRepository,
                      QuartierRepository quartierRepository,
                      EcoleRepository ecoleRepository,
                      AnneescolaireRepository anneescolaireRepository,
                      RoleRepository roleRepository,
                      UtilisateurRepository utilisateurRepository,
                      ClasseRepository classeRepository) {
        this.regionRepository = regionRepository;
        this.departementRepository = departementRepository;
        this.arrondissementRepository = arrondissementRepository;
        this.villeRepository = villeRepository;
        this.quartierRepository = quartierRepository;
        this.ecoleRepository = ecoleRepository;
        this.anneescolaireRepository = anneescolaireRepository;
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.classeRepository = classeRepository;
    }

    @Override
    public void run(String... args) {
        if (regionRepository.count() > 0) {
            return;
        }

        System.out.println("========== SEED : Insertion des données initiales ==========");

        // =====================================================================
        // 1. ADAMAOUA (AD)
        // =====================================================================
        Region adamaoua = createRegion("AD", "Adamaoua");

        Departement djere = createDepartement("DJR", "Djérem", adamaoua);
        Departement faro_et_deo = createDepartement("FRD", "Faro-et-Déo", adamaoua);
        Departement mayo_banyo = createDepartement("MBY", "Mayo-Banyo", adamaoua);
        Departement mbere = createDepartement("MBR", "Mbéré", adamaoua);
        Departement vina = createDepartement("VNA", "Vina", adamaoua);

        Arrondissement ngaoundere1 = createArrondissement("NDE1", "Ngaoundéré 1er", vina);
        Arrondissement ngaoundere2 = createArrondissement("NDE2", "Ngaoundéré 2ème", vina);
        Arrondissement ngaoundere3 = createArrondissement("NDE3", "Ngaoundéré 3ème", vina);
        Arrondissement tibati = createArrondissement("TBT", "Tibati", djere);
        Arrondissement tignere = createArrondissement("TGR", "Tignère", faro_et_deo);
        Arrondissement banyo = createArrondissement("BNY", "Banyo", mayo_banyo);
        Arrondissement meiganga = createArrondissement("MGG", "Meiganga", mbere);

        Ville ngaoundereVille = createVille("VNDE", "Ngaoundéré", ngaoundere1);
        Ville tibatiVille = createVille("VTBT", "Tibati", tibati);
        Ville banyoVille = createVille("VBNY", "Banyo", banyo);
        Ville meigangaVille = createVille("VMGG", "Meiganga", meiganga);
        Ville tignereVille = createVille("VTGR", "Tignère", tignere);

        Quartier joli_soir = createQuartier("QJS", "Joli-Soir", ngaoundereVille);
        Quartier baladji = createQuartier("QBL", "Baladji", ngaoundereVille);
        Quartier mbideng = createQuartier("QMB", "Mbideng", ngaoundereVille);
        Quartier onaref = createQuartier("QON", "Onaref", ngaoundereVille);
        Quartier banyoCentre = createQuartier("QBNC", "Centre Banyo", banyoVille);

        System.out.println("  -> Adamaoua : 5 départements, 7 arrondissements, 5 villes, 5 quartiers");

        // =====================================================================
        // 2. CENTRE (CE)
        // =====================================================================
        Region centre = createRegion("CE", "Centre");

        Departement haute_sanaga = createDepartement("HSG", "Haute-Sanaga", centre);
        Departement lekie = createDepartement("LKE", "Lékié", centre);
        Departement mbam_et_inoubou = createDepartement("MBI", "Mbam-et-Inoubou", centre);
        Departement mbam_et_kim = createDepartement("MBK", "Mbam-et-Kim", centre);
        Departement mefou_et_afamba = createDepartement("MFA", "Méfou-et-Afamba", centre);
        Departement mefou_et_akono = createDepartement("MAK", "Méfou-et-Akono", centre);
        Departement mfoundi = createDepartement("MFD", "Mfoundi", centre);
        Departement nyong_et_kellé = createDepartement("NYK", "Nyong-et-Kellé", centre);
        Departement nyong_et_mfoumou = createDepartement("NYM", "Nyong-et-Mfoumou", centre);
        Departement nyong_et_so = createDepartement("NYS", "Nyong-et-So'o", centre);

        Arrondissement yaounde1 = createArrondissement("YDE1", "Yaoundé 1er", mfoundi);
        Arrondissement yaounde2 = createArrondissement("YDE2", "Yaoundé 2ème", mfoundi);
        Arrondissement yaounde3 = createArrondissement("YDE3", "Yaoundé 3ème", mfoundi);
        Arrondissement yaounde4 = createArrondissement("YDE4", "Yaoundé 4ème", mfoundi);
        Arrondissement yaounde5 = createArrondissement("YDE5", "Yaoundé 5ème", mfoundi);
        Arrondissement yaounde6 = createArrondissement("YDE6", "Yaoundé 6ème", mfoundi);
        Arrondissement yaounde7 = createArrondissement("YDE7", "Yaoundé 7ème", mfoundi);
        Arrondissement monatele = createArrondissement("MNT", "Monatélé", lekie);
        Arrondissement obala = createArrondissement("OBL", "Obala", lekie);
        Arrondissement okola = createArrondissement("OKL", "Okola", lekie);
        Arrondissement saa = createArrondissement("SAA", "Saa", lekie);
        Arrondissement nanga_eboko = createArrondissement("NEB", "Nanga-Eboko", haute_sanaga);
        Arrondissement bafia = createArrondissement("BFA", "Bafia", mbam_et_inoubou);
        Arrondissement ntui = createArrondissement("NTU", "Ntui", mbam_et_kim);
        Arrondissement mfou = createArrondissement("MFU", "Mfou", mefou_et_afamba);
        Arrondissement ngoumou = createArrondissement("NGM", "Ngoumou", mefou_et_akono);
        Arrondissement eseka = createArrondissement("ESK", "Éséka", nyong_et_kellé);
        Arrondissement akonolinga = createArrondissement("AKN", "Akonolinga", nyong_et_mfoumou);
        Arrondissement mbalmayo = createArrondissement("MBM", "Mbalmayo", nyong_et_so);

        Ville yaoundeVille = createVille("VYDE", "Yaoundé", yaounde1);
        Ville obalaVille = createVille("VOBL", "Obala", obala);
        Ville monateleVille = createVille("VMNT", "Monatélé", monatele);
        Ville bafiaVille = createVille("VBFA", "Bafia", bafia);
        Ville mfouVille = createVille("VMFU", "Mfou", mfou);
        Ville mbalmayoVille = createVille("VMBM", "Mbalmayo", mbalmayo);
        Ville esekaVille = createVille("VESK", "Éséka", eseka);
        Ville nangaEbokoVille = createVille("VNEB", "Nanga-Eboko", nanga_eboko);
        Ville akonolingaVille = createVille("VAKN", "Akonolinga", akonolinga);

        Quartier bastos = createQuartier("QBS", "Bastos", yaoundeVille);
        Quartier nlongkak = createQuartier("QNL", "Nlongkak", yaoundeVille);
        Quartier mvog_mbi = createQuartier("QMM", "Mvog-Mbi", yaoundeVille);
        Quartier mvan = createQuartier("QMV", "Mvan", yaoundeVille);
        Quartier biyem_assi = createQuartier("QBA", "Biyem-Assi", yaoundeVille);
        Quartier essos = createQuartier("QES", "Essos", yaoundeVille);
        Quartier etoa_meki = createQuartier("QEM", "Étoa-Méki", yaoundeVille);
        Quartier nkolbisson = createQuartier("QNK", "Nkolbisson", yaoundeVille);
        Quartier messa = createQuartier("QMS", "Messa", yaoundeVille);
        Quartier omnisport = createQuartier("QOM", "Omnisport", yaoundeVille);
        Quartier mendong = createQuartier("QMD", "Mendong", yaoundeVille);
        Quartier nsimeyong = createQuartier("QNS", "Nsimeyong", yaoundeVille);
        Quartier mokolo = createQuartier("QMK", "Mokolo", yaoundeVille);
        Quartier tsinga = createQuartier("QTS", "Tsinga", yaoundeVille);
        Quartier ngousso = createQuartier("QNG", "Ngousso", yaoundeVille);
        Quartier emana = createQuartier("QEA", "Emana", yaoundeVille);
        Quartier obalaCentre = createQuartier("QOBC", "Centre Obala", obalaVille);
        Quartier bafiaCentre = createQuartier("QBFC", "Centre Bafia", bafiaVille);

        System.out.println("  -> Centre : 10 départements, 19 arrondissements, 9 villes, 18 quartiers");

        // =====================================================================
        // 3. EST (ES)
        // =====================================================================
        Region est = createRegion("ES", "Est");

        Departement boumba_et_ngoko = createDepartement("BNG", "Boumba-et-Ngoko", est);
        Departement haut_nyong = createDepartement("HNY", "Haut-Nyong", est);
        Departement kadey = createDepartement("KDY", "Kadey", est);
        Departement lom_et_djerem = createDepartement("LDJ", "Lom-et-Djérem", est);

        Arrondissement bertoua1 = createArrondissement("BTA1", "Bertoua 1er", lom_et_djerem);
        Arrondissement bertoua2 = createArrondissement("BTA2", "Bertoua 2ème", lom_et_djerem);
        Arrondissement abong_mbang = createArrondissement("ABM", "Abong-Mbang", haut_nyong);
        Arrondissement batouri = createArrondissement("BTR", "Batouri", kadey);
        Arrondissement yokadouma = createArrondissement("YKD", "Yokadouma", boumba_et_ngoko);
        Arrondissement belabo = createArrondissement("BLB", "Bélabo", lom_et_djerem);

        Ville bertouaVille = createVille("VBTA", "Bertoua", bertoua1);
        Ville abongMbangVille = createVille("VABM", "Abong-Mbang", abong_mbang);
        Ville batouriVille = createVille("VBTR", "Batouri", batouri);
        Ville yokadoumaVille = createVille("VYKD", "Yokadouma", yokadouma);

        Quartier bertouaCentre = createQuartier("QBTC", "Centre Bertoua", bertouaVille);
        Quartier haoussa_bertoua = createQuartier("QHB", "Haoussa", bertouaVille);
        Quartier batouriCentre = createQuartier("QBTRC", "Centre Batouri", batouriVille);

        System.out.println("  -> Est : 4 départements, 6 arrondissements, 4 villes, 3 quartiers");

        // =====================================================================
        // 4. EXTRÊME-NORD (EN)
        // =====================================================================
        Region extreme_nord = createRegion("EN", "Extrême-Nord");

        Departement diamare = createDepartement("DMR", "Diamaré", extreme_nord);
        Departement logone_et_chari = createDepartement("LGC", "Logone-et-Chari", extreme_nord);
        Departement mayo_danay = createDepartement("MYD", "Mayo-Danay", extreme_nord);
        Departement mayo_kani = createDepartement("MYK", "Mayo-Kani", extreme_nord);
        Departement mayo_sava = createDepartement("MYS", "Mayo-Sava", extreme_nord);
        Departement mayo_tsanaga = createDepartement("MYT", "Mayo-Tsanaga", extreme_nord);

        Arrondissement maroua1 = createArrondissement("MRA1", "Maroua 1er", diamare);
        Arrondissement maroua2 = createArrondissement("MRA2", "Maroua 2ème", diamare);
        Arrondissement maroua3 = createArrondissement("MRA3", "Maroua 3ème", diamare);
        Arrondissement kousseri = createArrondissement("KSR", "Kousseri", logone_et_chari);
        Arrondissement yagoua = createArrondissement("YGA", "Yagoua", mayo_danay);
        Arrondissement kaele = createArrondissement("KLE", "Kaélé", mayo_kani);
        Arrondissement mora = createArrondissement("MOR", "Mora", mayo_sava);
        Arrondissement mokolo_arr = createArrondissement("MKL", "Mokolo", mayo_tsanaga);

        Ville marouaVille = createVille("VMRA", "Maroua", maroua1);
        Ville kousseriVille = createVille("VKSR", "Kousseri", kousseri);
        Ville yagouaVille = createVille("VYGA", "Yagoua", yagoua);
        Ville kaeleVille = createVille("VKLE", "Kaélé", kaele);
        Ville moraVille = createVille("VMOR", "Mora", mora);
        Ville mokoloVille = createVille("VMKL", "Mokolo", mokolo_arr);

        Quartier domayo = createQuartier("QDM", "Domayo", marouaVille);
        Quartier pitoaré = createQuartier("QPT", "Pitoaré", marouaVille);
        Quartier kakataré = createQuartier("QKK", "Kakataré", marouaVille);
        Quartier pont_vert = createQuartier("QPV", "Pont-Vert", marouaVille);
        Quartier kousseriCentre = createQuartier("QKSC", "Centre Kousseri", kousseriVille);

        System.out.println("  -> Extrême-Nord : 6 départements, 8 arrondissements, 6 villes, 5 quartiers");

        // =====================================================================
        // 5. LITTORAL (LT)
        // =====================================================================
        Region littoral = createRegion("LT", "Littoral");

        Departement moungo = createDepartement("MNG", "Moungo", littoral);
        Departement nkam = createDepartement("NKM", "Nkam", littoral);
        Departement sanaga_maritime = createDepartement("SGM", "Sanaga-Maritime", littoral);
        Departement wouri = createDepartement("WRI", "Wouri", littoral);

        Arrondissement douala1 = createArrondissement("DLA1", "Douala 1er", wouri);
        Arrondissement douala2 = createArrondissement("DLA2", "Douala 2ème", wouri);
        Arrondissement douala3 = createArrondissement("DLA3", "Douala 3ème", wouri);
        Arrondissement douala4 = createArrondissement("DLA4", "Douala 4ème", wouri);
        Arrondissement douala5 = createArrondissement("DLA5", "Douala 5ème", wouri);
        Arrondissement nkongsamba1 = createArrondissement("NKS1", "Nkongsamba 1er", moungo);
        Arrondissement nkongsamba2 = createArrondissement("NKS2", "Nkongsamba 2ème", moungo);
        Arrondissement loum = createArrondissement("LOM", "Loum", moungo);
        Arrondissement manjo = createArrondissement("MNJ", "Manjo", moungo);
        Arrondissement edea1 = createArrondissement("EDA1", "Édéa 1er", sanaga_maritime);
        Arrondissement edea2 = createArrondissement("EDA2", "Édéa 2ème", sanaga_maritime);
        Arrondissement yabassi = createArrondissement("YBS", "Yabassi", nkam);

        Ville doualaVille = createVille("VDLA", "Douala", douala1);
        Ville nkongsamba = createVille("VNKS", "Nkongsamba", nkongsamba1);
        Ville loumVille = createVille("VLOM", "Loum", loum);
        Ville edeaVille = createVille("VEDA", "Édéa", edea1);
        Ville yabassiVille = createVille("VYBS", "Yabassi", yabassi);

        Quartier bonamoussadi = createQuartier("QBN", "Bonamoussadi", doualaVille);
        Quartier akwa = createQuartier("QAK", "Akwa", doualaVille);
        Quartier deido = createQuartier("QDD", "Deido", doualaVille);
        Quartier bali = createQuartier("QBL2", "Bali", doualaVille);
        Quartier bonaberi = createQuartier("QBB", "Bonabéri", doualaVille);
        Quartier new_bell = createQuartier("QNB", "New-Bell", doualaVille);
        Quartier bonapriso = createQuartier("QBP", "Bonapriso", doualaVille);
        Quartier makepe = createQuartier("QMK2", "Makepe", doualaVille);
        Quartier logpom = createQuartier("QLG", "Logpom", doualaVille);
        Quartier kotto = createQuartier("QKT", "Kotto", doualaVille);
        Quartier ndogbong = createQuartier("QND", "Ndogbong", doualaVille);
        Quartier cite_sic = createQuartier("QCS", "Cité SIC", doualaVille);
        Quartier nkongsambaCentre = createQuartier("QNKC", "Centre Nkongsamba", nkongsamba);

        System.out.println("  -> Littoral : 4 départements, 12 arrondissements, 5 villes, 13 quartiers");

        // =====================================================================
        // 6. NORD (NO)
        // =====================================================================
        Region nord = createRegion("NO", "Nord");

        Departement benoue = createDepartement("BNE", "Bénoué", nord);
        Departement faro = createDepartement("FAR", "Faro", nord);
        Departement mayo_louti = createDepartement("MYL", "Mayo-Louti", nord);
        Departement mayo_rey = createDepartement("MYR", "Mayo-Rey", nord);

        Arrondissement garoua1 = createArrondissement("GRA1", "Garoua 1er", benoue);
        Arrondissement garoua2 = createArrondissement("GRA2", "Garoua 2ème", benoue);
        Arrondissement garoua3 = createArrondissement("GRA3", "Garoua 3ème", benoue);
        Arrondissement poli = createArrondissement("POL", "Poli", faro);
        Arrondissement guider = createArrondissement("GDR", "Guider", mayo_louti);
        Arrondissement tcholliré = createArrondissement("TCL", "Tchollire", mayo_rey);
        Arrondissement rey_bouba = createArrondissement("RYB", "Rey-Bouba", mayo_rey);

        Ville garouaVille = createVille("VGRA", "Garoua", garoua1);
        Ville guiderVille = createVille("VGDR", "Guider", guider);
        Ville tchollireVille = createVille("VTCL", "Tchollire", tcholliré);

        Quartier yelwa = createQuartier("QYL", "Yelwa", garouaVille);
        Quartier roumdé = createQuartier("QRM", "Roumdé-Adjia", garouaVille);
        Quartier lopéré = createQuartier("QLP", "Lopéré", garouaVille);
        Quartier poumpoumré = createQuartier("QPP", "Poumpoumré", garouaVille);

        System.out.println("  -> Nord : 4 départements, 7 arrondissements, 3 villes, 4 quartiers");

        // =====================================================================
        // 7. NORD-OUEST (NW)
        // =====================================================================
        Region nord_ouest = createRegion("NW", "Nord-Ouest");

        Departement boyo = createDepartement("BYO", "Boyo", nord_ouest);
        Departement bui = createDepartement("BUI", "Bui", nord_ouest);
        Departement donga_mantung = createDepartement("DGM", "Donga-Mantung", nord_ouest);
        Departement menchum = createDepartement("MCH", "Menchum", nord_ouest);
        Departement mezam = createDepartement("MZM", "Mezam", nord_ouest);
        Departement momo = createDepartement("MMO", "Momo", nord_ouest);
        Departement ngo_ketunjia = createDepartement("NGK", "Ngo-Ketunjia", nord_ouest);

        Arrondissement bamenda1 = createArrondissement("BDA1", "Bamenda 1er", mezam);
        Arrondissement bamenda2 = createArrondissement("BDA2", "Bamenda 2ème", mezam);
        Arrondissement bamenda3 = createArrondissement("BDA3", "Bamenda 3ème", mezam);
        Arrondissement kumbo = createArrondissement("KMB", "Kumbo", bui);
        Arrondissement nkambe = createArrondissement("NKB", "Nkambe", donga_mantung);
        Arrondissement wum = createArrondissement("WUM", "Wum", menchum);
        Arrondissement fundong = createArrondissement("FDG", "Fundong", boyo);
        Arrondissement mbengwi = createArrondissement("MBW", "Mbengwi", momo);
        Arrondissement ndop = createArrondissement("NDP", "Ndop", ngo_ketunjia);

        Ville bamendaVille = createVille("VBDA", "Bamenda", bamenda1);
        Ville kumboVille = createVille("VKMB", "Kumbo", kumbo);
        Ville nkambeVille = createVille("VNKB", "Nkambe", nkambe);
        Ville wumVille = createVille("VWUM", "Wum", wum);
        Ville ndopVille = createVille("VNDP", "Ndop", ndop);

        Quartier nkwen = createQuartier("QNKW", "Nkwen", bamendaVille);
        Quartier up_station = createQuartier("QUP", "Up Station", bamendaVille);
        Quartier old_town = createQuartier("QOT", "Old Town", bamendaVille);
        Quartier commercial_avenue = createQuartier("QCA", "Commercial Avenue", bamendaVille);
        Quartier mile_4 = createQuartier("QM4", "Mile 4 Nkwen", bamendaVille);
        Quartier kumboCentre = createQuartier("QKBC", "Centre Kumbo", kumboVille);

        System.out.println("  -> Nord-Ouest : 7 départements, 9 arrondissements, 5 villes, 6 quartiers");

        // =====================================================================
        // 8. OUEST (OU)
        // =====================================================================
        Region ouest = createRegion("OU", "Ouest");

        Departement bamboutos = createDepartement("BMB", "Bamboutos", ouest);
        Departement haut_nkam = createDepartement("HNK", "Haut-Nkam", ouest);
        Departement hauts_plateaux = createDepartement("HPL", "Hauts-Plateaux", ouest);
        Departement koung_khi = createDepartement("KKH", "Koung-Khi", ouest);
        Departement menoua = createDepartement("MNA", "Menoua", ouest);
        Departement mifi = createDepartement("MFI", "Mifi", ouest);
        Departement nde = createDepartement("NDE", "Ndé", ouest);
        Departement noun = createDepartement("NON", "Noun", ouest);

        Arrondissement bafoussam1 = createArrondissement("BFS1", "Bafoussam 1er", mifi);
        Arrondissement bafoussam2 = createArrondissement("BFS2", "Bafoussam 2ème", mifi);
        Arrondissement bafoussam3 = createArrondissement("BFS3", "Bafoussam 3ème", mifi);
        Arrondissement dschang = createArrondissement("DSC", "Dschang", menoua);
        Arrondissement mbouda = createArrondissement("MBD", "Mbouda", bamboutos);
        Arrondissement bangangte = createArrondissement("BGT", "Bangangté", nde);
        Arrondissement foumban = createArrondissement("FMB", "Foumban", noun);
        Arrondissement foumbot = createArrondissement("FMT", "Foumbot", noun);
        Arrondissement bafang = createArrondissement("BFG", "Bafang", haut_nkam);
        Arrondissement bandjoun = createArrondissement("BDJ", "Bandjoun", koung_khi);
        Arrondissement baham = createArrondissement("BHM", "Baham", hauts_plateaux);

        Ville bafoussamVille = createVille("VBFS", "Bafoussam", bafoussam1);
        Ville dschangVille = createVille("VDSC", "Dschang", dschang);
        Ville mboudaVille = createVille("VMBD", "Mbouda", mbouda);
        Ville foumbanVille = createVille("VFMB", "Foumban", foumban);
        Ville bangangteVille = createVille("VBGT", "Bangangté", bangangte);
        Ville bafangVille = createVille("VBFG", "Bafang", bafang);
        Ville bandjounVille = createVille("VBDJ", "Bandjoun", bandjoun);

        Quartier tamdja = createQuartier("QTD", "Tamdja", bafoussamVille);
        Quartier djeleng = createQuartier("QDJ", "Djeleng", bafoussamVille);
        Quartier kamkop = createQuartier("QKP", "Kamkop", bafoussamVille);
        Quartier king_place = createQuartier("QKG", "King-Place", bafoussamVille);
        Quartier tougang = createQuartier("QTG", "Tougang", bafoussamVille);
        Quartier dschangCentre = createQuartier("QDSC", "Centre Dschang", dschangVille);
        Quartier foumbanCentre = createQuartier("QFMC", "Centre Foumban", foumbanVille);

        System.out.println("  -> Ouest : 8 départements, 11 arrondissements, 7 villes, 7 quartiers");

        // =====================================================================
        // 9. SUD (SU)
        // =====================================================================
        Region sud = createRegion("SU", "Sud");

        Departement dja_et_lobo = createDepartement("DJL", "Dja-et-Lobo", sud);
        Departement mvila = createDepartement("MVL", "Mvila", sud);
        Departement ocean = createDepartement("OCN", "Océan", sud);
        Departement vallee_du_ntem = createDepartement("VDN", "Vallée-du-Ntem", sud);

        Arrondissement ebolowa1 = createArrondissement("EBW1", "Ebolowa 1er", mvila);
        Arrondissement ebolowa2 = createArrondissement("EBW2", "Ebolowa 2ème", mvila);
        Arrondissement kribi1 = createArrondissement("KRB1", "Kribi 1er", ocean);
        Arrondissement kribi2 = createArrondissement("KRB2", "Kribi 2ème", ocean);
        Arrondissement sangmelima = createArrondissement("SGM2", "Sangmélima", dja_et_lobo);
        Arrondissement ambam = createArrondissement("AMB", "Ambam", vallee_du_ntem);

        Ville ebolowaVille = createVille("VEBW", "Ebolowa", ebolowa1);
        Ville kribiVille = createVille("VKRB", "Kribi", kribi1);
        Ville sangmelimaVille = createVille("VSGM", "Sangmélima", sangmelima);
        Ville ambamVille = createVille("VAMB", "Ambam", ambam);

        Quartier ebolowaCentre = createQuartier("QEBC", "Centre Ebolowa", ebolowaVille);
        Quartier nko_oveng = createQuartier("QNKO", "Nko'oveng", ebolowaVille);
        Quartier kribiCentre = createQuartier("QKRC", "Centre Kribi", kribiVille);

        System.out.println("  -> Sud : 4 départements, 6 arrondissements, 4 villes, 3 quartiers");

        // =====================================================================
        // 10. SUD-OUEST (SW)
        // =====================================================================
        Region sud_ouest = createRegion("SW", "Sud-Ouest");

        Departement fako = createDepartement("FKO", "Fako", sud_ouest);
        Departement kupe_muanenguba = createDepartement("KPM", "Koupé-Manengouba", sud_ouest);
        Departement lebialem = createDepartement("LBL", "Lebialem", sud_ouest);
        Departement manyu = createDepartement("MNY", "Manyu", sud_ouest);
        Departement meme = createDepartement("MME", "Meme", sud_ouest);
        Departement ndian = createDepartement("NDN", "Ndian", sud_ouest);

        Arrondissement buea = createArrondissement("BUE", "Buea", fako);
        Arrondissement limbe1 = createArrondissement("LMB1", "Limbe 1er", fako);
        Arrondissement limbe2 = createArrondissement("LMB2", "Limbe 2ème", fako);
        Arrondissement tiko = createArrondissement("TKO", "Tiko", fako);
        Arrondissement kumba = createArrondissement("KBA", "Kumba", meme);
        Arrondissement mamfe = createArrondissement("MMF", "Mamfe", manyu);
        Arrondissement bangem = createArrondissement("BGM", "Bangem", kupe_muanenguba);
        Arrondissement mundemba = createArrondissement("MDM", "Mundemba", ndian);

        Ville bueaVille = createVille("VBUE", "Buea", buea);
        Ville limbeVille = createVille("VLMB", "Limbe", limbe1);
        Ville kumbaVille = createVille("VKBA", "Kumba", kumba);
        Ville mamfeVille = createVille("VMMF", "Mamfe", mamfe);
        Ville tikoVille = createVille("VTKO", "Tiko", tiko);

        Quartier molyko = createQuartier("QML", "Molyko", bueaVille);
        Quartier great_soppo = createQuartier("QGS", "Great Soppo", bueaVille);
        Quartier buea_town = createQuartier("QBT", "Buea Town", bueaVille);
        Quartier bonduma = createQuartier("QBD", "Bonduma", bueaVille);
        Quartier limbe_down_beach = createQuartier("QLDB", "Down Beach", limbeVille);
        Quartier limbeCentre = createQuartier("QLBC", "Centre Limbe", limbeVille);
        Quartier kumbaCentre = createQuartier("QKBAC", "Centre Kumba", kumbaVille);

        System.out.println("  -> Sud-Ouest : 6 départements, 8 arrondissements, 5 villes, 7 quartiers");

        // =====================================================================
        // RÔLES
        // =====================================================================

        Role roleAdmin = createRole("ADMIN");
        Role roleDirecteur = createRole("DIRECTEUR");
        Role roleEnseignant = createRole("ENSEIGNANT");
        Role roleSecretaire = createRole("SECRETAIRE");
        Role roleParent = createRole("PARENT");

        System.out.println("  -> Rôles : 5 rôles créés");

        // =====================================================================
        // ÉCOLES
        // =====================================================================

        Ecole ecoleBilingue = createEcole("ECB-001", "École Bilingue La Victoire", "BP 1234, Yaoundé",
                "+237 677 123 456", "contact@lavictoire.cm", true, bastos);

        Ecole ecoleAnglo = createEcole("ECA-001", "Progressive Comprehensive College", "BP 5678, Douala",
                "+237 699 987 654", "info@progressive.cm", true, bonamoussadi);

        Ecole ecoleFranco = createEcole("ECF-001", "Groupe Scolaire Les Champions", "BP 9012, Bafoussam",
                "+237 655 456 789", "direction@leschampions.cm", true, tamdja);

        System.out.println("  -> Écoles : 3 écoles créées");

        // =====================================================================
        // ANNÉES SCOLAIRES
        // =====================================================================

        Anneescolaire annee2024 = createAnneeScolaire("2024-2025",
                LocalDate.of(2024, 9, 2), LocalDate.of(2025, 6, 30), false);

        Anneescolaire annee2025 = createAnneeScolaire("2025-2026",
                LocalDate.of(2025, 9, 1), LocalDate.of(2026, 6, 30), true);

        System.out.println("  -> Années scolaires : 2024-2025 (archivée), 2025-2026 (active)");

        // =====================================================================
        // UTILISATEURS
        // =====================================================================

        Utilisateur enseignant1 = createUtilisateur("Kamga", "Jean-Pierre", "jpkamga@lavictoire.cm",
                "password123", true, roleEnseignant, ecoleBilingue);

        Utilisateur enseignant2 = createUtilisateur("Ngo Bassa", "Marie", "mngo@lavictoire.cm",
                "password123", true, roleEnseignant, ecoleBilingue);

        Utilisateur enseignant3 = createUtilisateur("Fotso", "Paul", "pfotso@leschampions.cm",
                "password123", true, roleEnseignant, ecoleFranco);

        Utilisateur enseignant4 = createUtilisateur("Njoya", "Grace", "gnjoya@progressive.cm",
                "password123", true, roleEnseignant, ecoleAnglo);

        Utilisateur directeur1 = createUtilisateur("Mbarga", "Samuel", "smbarga@lavictoire.cm",
                "password123", true, roleDirecteur, ecoleBilingue);

        Utilisateur secretaire1 = createUtilisateur("Atangana", "Chantal", "catangana@lavictoire.cm",
                "password123", true, roleSecretaire, ecoleBilingue);

        System.out.println("  -> Utilisateurs : 4 enseignants, 1 directeur, 1 secrétaire");

        // =====================================================================
        // CLASSES
        // =====================================================================

        // -- École Bilingue La Victoire (Francophone + Anglophone) --
        createClasse("SIL-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 45, 25000.0,
                "Section d'Initiation au Langage", ecoleBilingue, annee2025, enseignant1);
        createClasse("CP-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 45, 25000.0,
                "Cours Préparatoire", ecoleBilingue, annee2025, enseignant2);
        createClasse("CE1-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 25000.0,
                null, ecoleBilingue, annee2025, null);
        createClasse("CE2-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 25000.0,
                null, ecoleBilingue, annee2025, null);
        createClasse("CM1-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 30000.0,
                null, ecoleBilingue, annee2025, null);
        createClasse("CM2-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 30000.0,
                "Classe d'examen CEP", ecoleBilingue, annee2025, null);

        createClasse("Class 1-A", Niveau.PRIMARY, SousSysteme.ANGLOPHONE, "A", 40, 30000.0,
                null, ecoleBilingue, annee2025, null);
        createClasse("Class 6-A", Niveau.PRIMARY, SousSysteme.ANGLOPHONE, "A", 40, 35000.0,
                "Exam class - FSLC", ecoleBilingue, annee2025, null);

        createClasse("Petite Section", Niveau.MATERNELLE, SousSysteme.FRANCOPHONE, null, 30, 20000.0,
                "3-4 ans", ecoleBilingue, annee2025, null);
        createClasse("Grande Section", Niveau.MATERNELLE, SousSysteme.FRANCOPHONE, null, 30, 20000.0,
                "5-6 ans", ecoleBilingue, annee2025, null);

        // -- Progressive Comprehensive College (Anglophone) --
        createClasse("Form 1-A", Niveau.SECONDARY, SousSysteme.ANGLOPHONE, "A", 60, 50000.0,
                null, ecoleAnglo, annee2025, enseignant4);
        createClasse("Form 5-A", Niveau.SECONDARY, SousSysteme.ANGLOPHONE, "A", 55, 55000.0,
                "Exam class - GCE O/L", ecoleAnglo, annee2025, null);
        createClasse("Lower 6th Science", Niveau.HIGH_SCHOOL, SousSysteme.ANGLOPHONE, null, 45, 65000.0,
                "Sciences", ecoleAnglo, annee2025, null);
        createClasse("Upper 6th Arts", Niveau.HIGH_SCHOOL, SousSysteme.ANGLOPHONE, null, 40, 65000.0,
                "GCE A/L Arts", ecoleAnglo, annee2025, null);

        // -- Groupe Scolaire Les Champions (Francophone) --
        createClasse("6ème-A", Niveau.COLLEGE, SousSysteme.FRANCOPHONE, "A", 60, 45000.0,
                null, ecoleFranco, annee2025, enseignant3);
        createClasse("6ème-B", Niveau.COLLEGE, SousSysteme.FRANCOPHONE, "B", 60, 45000.0,
                null, ecoleFranco, annee2025, null);
        createClasse("3ème-A", Niveau.COLLEGE, SousSysteme.FRANCOPHONE, "A", 55, 50000.0,
                "Classe d'examen BEPC", ecoleFranco, annee2025, null);
        createClasse("Tle D", Niveau.LYCEE, SousSysteme.FRANCOPHONE, null, 50, 60000.0,
                "Terminale Sciences - Baccalauréat", ecoleFranco, annee2025, null);
        createClasse("Tle A4", Niveau.LYCEE, SousSysteme.FRANCOPHONE, null, 50, 60000.0,
                "Terminale Lettres - Baccalauréat", ecoleFranco, annee2025, null);

        // -- Classe archivée --
        Classe classeArchivee = createClasse("CM2-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 48, 22000.0,
                "Classe 2024-2025 archivée", ecoleBilingue, annee2024, null);
        classeArchivee.setStatut(StatutClasse.ARCHIVEE);
        classeRepository.save(classeArchivee);

        System.out.println("  -> Classes : 20 classes créées (19 actives + 1 archivée)");

        System.out.println("========== SEED : Terminé ==========");
    }

    // ==================== HELPERS ====================

    private Region createRegion(String code, String nom) {
        Region r = new Region();
        r.setCode(code);
        r.setNom(nom);
        return regionRepository.save(r);
    }

    private Departement createDepartement(String code, String nom, Region region) {
        Departement d = new Departement();
        d.setCode(code);
        d.setNom(nom);
        d.setRegion(region);
        return departementRepository.save(d);
    }

    private Arrondissement createArrondissement(String code, String nom, Departement departement) {
        Arrondissement a = new Arrondissement();
        a.setCode(code);
        a.setNom(nom);
        a.setDepartement(departement);
        return arrondissementRepository.save(a);
    }

    private Ville createVille(String code, String nom, Arrondissement arrondissement) {
        Ville v = new Ville();
        v.setCode(code);
        v.setNom(nom);
        v.setArrondissement(arrondissement);
        return villeRepository.save(v);
    }

    private Quartier createQuartier(String code, String nom, Ville ville) {
        Quartier q = new Quartier();
        q.setCode(code);
        q.setNom(nom);
        q.setVille(ville);
        return quartierRepository.save(q);
    }

    private Role createRole(String nomRole) {
        Role r = new Role();
        r.setNomRole(nomRole);
        return roleRepository.save(r);
    }

    private Ecole createEcole(String codeEcole, String nom, String adresse,
                               String telephone, String email, Boolean statut, Quartier quartier) {
        Ecole e = new Ecole();
        e.setCodeEcole(codeEcole);
        e.setNom(nom);
        e.setAdresse(adresse);
        e.setTelephone(telephone);
        e.setEmail(email);
        e.setStatut(statut);
        e.setQuartier(quartier);
        e.setTenant(TENANT);
        return ecoleRepository.save(e);
    }

    private Anneescolaire createAnneeScolaire(String libelle, LocalDate debut, LocalDate fin, Boolean statut) {
        Anneescolaire a = new Anneescolaire();
        a.setLibelle(libelle);
        a.setDateDebut(debut);
        a.setDateFin(fin);
        a.setStatut(statut);
        a.setTenant(TENANT);
        return anneescolaireRepository.save(a);
    }

    private Utilisateur createUtilisateur(String nom, String prenom, String email,
                                           String motDePasse, Boolean statut, Role role, Ecole ecole) {
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setMotDePasse(motDePasse);
        u.setStatut(statut);
        u.setRole(role);
        u.setEcole(ecole);
        return utilisateurRepository.save(u);
    }

    private Classe createClasse(String nomClasse, Niveau niveau, SousSysteme sousSysteme,
                                 String section, Integer capacite, Double fraisScolarite,
                                 String description, Ecole ecole, Anneescolaire annee,
                                 Utilisateur titulaire) {
        Classe c = new Classe();
        c.setNomClasse(nomClasse);
        c.setNiveau(niveau);
        c.setSousSysteme(sousSysteme);
        c.setSection(section);
        c.setCapacite(capacite);
        c.setFraisScolarite(fraisScolarite);
        c.setDescription(description);
        c.setStatut(StatutClasse.ACTIVE);
        c.setEcole(ecole);
        c.setAnneeScolaire(annee);
        c.setTitulaire(titulaire);
        c.setTenant(TENANT);
        return classeRepository.save(c);
    }
}
