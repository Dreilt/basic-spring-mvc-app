package pl.dreilt.basicspringmvcapp.service;

import pl.dreilt.basicspringmvcapp.dto.NewEventDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.entity.EventImage;
import pl.dreilt.basicspringmvcapp.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.enumeration.EventType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.*;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createUserRole;

public class EventServiceTestHelper {
    static final LocalDateTime DATE_TIME = LocalDateTime.now();

    static NewEventDto createNewEventDto() {
        return new NewEventDto.NewEventDtoBuilder()
                .withName("Java Dev Talks #1")
                .withEventImage(null)
                .withEventType(EventType.MEETING.getName())
                .withDateAndTime("2023-01-03T18:00")
                .withLanguage("polski")
                .withAdmission(AdmissionType.FREE.getName())
                .withCity("Kraków")
                .withLocation("Politechnika Krakowska")
                .withAddress("Warszawska 24, 31-155 Kraków")
                .withDescription("Spotkanie krakowskiej grupy pasjonatów języka Java.")
                .build();

    }

    static Event createEvent(Long id, String name, LocalDateTime dateAndTime, List<AppUser> participants) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        event.setEventType(EventType.MEETING.getName());
        event.setDateAndTime(dateAndTime);
        event.setLanguage("polski");
        event.setAdmission(AdmissionType.FREE.getName());
        event.setCity("Rzeszów");
        event.setLocation("WSIiZ");
        event.setAddress("Sucharskiego 2, 35-225 Rzeszów");
        event.setOrganizer(createAppUser(1L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole()));
        event.setDescription("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
        event.setEventImage(createEventImage());
        event.setParticipants(participants);
        return event;
    }

    private static EventImage createEventImage() {
        EventImage eventImage = new EventImage();
        eventImage.setId(1L);
        eventImage.setFileName("default_profile_image.png");
        eventImage.setFileType("image/png");
        String eventImageString = "89504E470D0A1A0A0000000D4948445200000200000002000806000000F478D4FA0000000473424954080808087C086488000000097048597300000DD700000DD70142289B780000001974455874536F667477617265007777772E696E6B73636170652E6F72679BEE3C1A0000200049444154789CEDDD79B425657DEEF16F3720A3E2800222D04C0A0A974164723A412D15318213D15797265A464D4C8CD19898C9DC385EBD2E6FE27548E52641ADC8A0C10C1A5378A3629451A36082A04223828AF13288745A8673FFD8BB731AE8610F55FB57C3F7B3168BE6F43E7B3FACB56BBFCF7EDFB7AA562D2F2F23A93FB2943F08D8037808B0FBF8DF1BFF79E39F6D0FFCE73DFE59B799FF5E07DC005C0F5C37FEF7F5C0F55559AC5FCCFF9DA4BAACB20048DD94A57C5BE060E070E088F13F87030F0E88F363EE5E0CAE012E032E05AEAACAC20F1AA9652C0052076429DF19388ABB0FF48732FA06DF763F6554063614824B814BABB2B829349534701600A9A5B2941F0C3C1D3809783CDD18ECA7712DF025E05CE0B355597C37388F34281600A925B294EF049CC868D07F3AB05F6CA285BB927119003E5795C5CDC179A45EB3004881B294EF0B9CCA68C07F22FDFB963FAB3B818B807F023E5695C595C179A4DEB100480B96A5FC3EC029C0CB802703AB631375C2C5C0478133AAB2B8213A8CD40716006941B2943F8AD1A0FF6260B7E0385D750750312A037F5B95C56DC179A4CEB200480DCA52BE0B701AF072E0B8E0387D732BF0D7C0BBABB2F8567418A96B2C005203B2943F1C7803F00BC02EC171FAEE2EE013C03BABB2F84A7418A92B2C00528DC603FFEF032F00B6098E3344E7322A02FF373A88D4761600A90659CA0F6234F0BF1007FE36B80478535516E7460791DACA0220CD214BF9818C06FE84037F1B9D03BCAE2A8BB5D141A4B6B1004833180FFCBF07BC0807FEB65B07BC93D1D2C07F468791DAC202204D61BCABFFBF03AF01B60D8EA3E9AC057EA32A8B4F460791DAC002204D284BF9CF03EF03F68ECEA2B99C09BCA22A8B5BA28348912C00D2566429DF0BF8534697EC553F5C0DBCA02A8B0BA38348512C00D26664295F0DFC2AF016E0BEC17154BF3B18EDE3F81F5559F841A8C1B100489B90A5FC28E043C0D1D159D4B8738117798F010D8D0540DAC8F8463D6F035E8BBBFB87E46AE0A95E525843620190C6C6B7E63D0B38263A8B42FC0838A92A8B4BA283488BE06D4825204BF93380AFE2E03F640F063E97A5FCA9D141A4457006408396A57C1B469BFCDE08AC0A8EA376B81DF8C5AA2CCAE82052932C001AAC2CE57B021F039E189D45AD7317705A55161F8F0E2235C502A041CA527E22A37BC9EF1E9D45ADB51E78867716545FB907408393A5FC4D8C4EFD72F0D7966C0F9C93A5FCD1D141A4263803A0C1185FD8E703C02BA2B3A8537E043CAE2A8B2BA3834875B2006810B2946FCF68CAFFD9D159D4495700475765716B7410A92E2E01A8F7B294DF0FF80C0EFE9ADD23800F468790EA640150AF6529DF1DF83CB0149B443D90B294E7D121A4BAB804A0DECA52BE3F5001074467516FFC27706C551697460791E66501502F65293F9CD1B4FF1ED159D43B970147556571477410691E2E01A877B2941F0F7C01077F35E330E0D7A24348F3720640BD92A5FC50E03CE001D159D46B3F010EAECAE2FAE820D2AC9C01506F8CEFE6F74F38F8AB79F705DE131D429A873300EA852CE50F06FE05787874160DCAB155595C141D429A853300EABC2CE5F705FE11077F2DDE6BA30348B37206409D36BEC2DFA78113A3B368906E07F6ABCAE2BAE820D2B49C0150678DAFED5FE2E0AF38DB01AF8E0E21CDC202A02EFB00F09CE8101ABC576429DF213A84342D0B803A294BF9AFE15DFDD40EBB01293A84342D0B803A274BF963807745E79036F2EBD101A4695900D42959CAEF0F9C05DC273A8BB491C3B294BB17459D620150D7FC15B0263883B4292E03A8532C00EA8C2CE5AF039E159D43DA8CC7450790A6E17500D40959CA8F63748DFFEDA2B3485BF090AA2C7E141D429A8433006ABD2CE50F04CEC4C15FED7742740069521600B55A96F255C087817DA2B34813786C740069521600B5DD2B81674487902664015067B80740AD95A57C4FE07260D7E82CD284D603BB5665B13E3A88B435CE00A8CDFE170EFEEA96ED81D3B394EF181D44DA1A6700D44A59CA4F023E159D439AD1578067799740B5990540AD93A57C27E0DF817DA3B34873F83E706A55161746079136C52500B5D11FE1E0AFEEDB13F84296F253A383489BE20C805A254BF911C0C5C0B6D159A49ADCCE6826C0252DB58A33006A8D2CE5AB810FE1E0AF7ED90EF87896F227450791366601509BBC0A38263A84D4801D80BFCB52EEFD02D41A2E01A815C61BFFAE061E129D456AD02DC0E3ABB2B8343A88E40C80DAE2D538F8ABFFEE079C99A57CE7E820920540E1C6DFFEDF109D435A9083813F890E215900D4067EFBD7D0FC5296F25F880EA161730F8042B9F6AF01BB0538A22A8BABA38368989C015034BFFD6BA8EE077C747CCB6B69E12C000AE3DABFC409C073A34368982C008AE4B77F09DE9AA5DC8B5F69E12C000AE1B77FE9BF1C04BC3C3A8486C702A0282FC06FFFD2067F302EC5D2C2580014258F0E20B5C89EC06BA34368583C0D500B97A5FC30C04BA14A777733B04F5516B74407D1303803A008AE774AF7B62B705A74080D8705400B95A57C07E045D139A4967A6974000D8705408BF66CE081D121A4963A214BF941D121340C16002D9A9BFFA42D7B6974000D839B00B53059CA0F04AE04BCF4A9B479D7026BAAB2B82B3A88FACD19002DD2CB71F097B6666FE049D121D47F16002D4496F2D5C04BA273481DE1B1A2C65900B42847037B4487903AE2A9DE25504DB30068519E161D40EA90DD8043A243A8DF2C005A94A74707903AE6F1D101D46F1600352E4BF9038163A273481DF384E800EA370B801621C3F79A342D0B801AE587B216C1E97F697A0FCB52BE263A84FACB02A0468D77323F353A87D451CE02A831160035ED4860F7E81052475900D4180B809AE6E97FD2EC8E8F0EA0FEB200A8694EFF4BB33BC00B02A92916003566FCC17554740EA9C3B607F68A0EA17EB200A8490700BB4487903A6EFFE800EA270B809A74447400A907F68B0EA07EB200A8491600697ECE00A811160035C90220CDCF02A0465800D4A4C3A303483DE012801A61015023B2943F087858740EA9079C0150232C006A8AD3FF523DF6C852BE637408F58F05404DB10048F55805EC1B1D42FD630150532C00527DEE1F1D40FD63015053DCB824D567A7E800EA1F0B809AF290E800528F5800543B0B809AE22D80A5FA5800543B0B806A97A57C07E07ED139A41EB100A876160035C1E97FA95E1600D5CE02A0265800A47A5900543B0B809A600190EA650150ED2C006A821B00A57A5900543B0B809AE00C80542F0B806A670150139C0190EA650150ED2C006AC283A303483DE3CD80543B0B809AB05D7400A967B6890EA0FEB100489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D90054092A401B200489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D90054092A401B200489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D90054092A401B200489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D90054092A401B200489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D90054092A401B200489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D90054092A401B200489234401600499206C8022049D200590024491A200B802449036401902469802C0092240D900540B5CA52BE1F706C740EA9678E1D1F5B526D2C00AA4D96F2A381F38135C151A4BE59039C3F3EC6A45A5800548B2CE527039F07760F8E22F5D5EEC0E7C7C79A34370B80E696A5FCF9C027819DA3B3483DB733F0C9F13127CD65D5F2F2727406755896F223802F013B45679106E436E0F8AA2C2E8D0EA2EEB200686659CA1F045C826BFE5284EF004757657153741075934B009A4996F26D803370F097A21C009459CAFD1CD74C7CE36856AF019E1C1D421AB893805F8E0EA16E72094053CB527E3FE02AE041D159247103706055163F890EA26E710640B378230EFE525B3C84D131294DC519004D254BF943816FE1AE7FA94DD601075565715D7410758733009AD6EB70F097DA6647E0F5D121D42DCE006862E3DDC6DF05F68ACE22E95EAE03F6AECAC20F754DC419004DE30938F84B6DB517F0B8E810EA0E0B80A6F10BD101246D919708D6C42C009AC8F8C23FCF8DCE21698B3C4635310B8026F5303CF54F6ABB3DB2947B474E4DC402A049ED131D40D2443C5635110B8026F5C0E8009226E2B1AA89580034A96BA303489A88C7AA266201D0A4AE8E0E206922D74407503758003491AA2C6E046E8ECE21698B7E5495C54FA343A81B2C009A86B30052BBAD8D0EA0EEB000681A1600A9DDD6460750775800340D0B80D46E1EA39A980540D3581B1D40D216AD8D0EA0EEB000681A7EBB90DA6D6D740075870540D3B00048EDB6363A80BAC302A069AC8D0E20698BBC0680266601D0C4C6E717DF109D43D226DD5095C56DD121D41D16004DEBCAE8009236C9635353B100685A97440790B4491E9B9A8A0540D3BA383A80A44DF2D8D4542C009A961F32523B796C6A2A16004DEBDBC04DD12124DDCD4D8C8E4D696216004DA52A8B65E02BD13924DDCD25E363539A980540B370AA516A178F494DCD02A059F86123B58BC7A4A66601D02CFCB091DAC56352535BB5BCECB291A697A5FC07C0EED13924F183AA2CF68C0EA1EE710640B3F2A223523BF8ED5F33B10068567EE848EDE0B1A899580034AB2F46079004C079D101D44D1600CDEA5F805BA3434803770BF0E5E810EA260B80665295C5CF807F8ECE210DDC67ABB2B83D3A84BAC902A079FC63740069E03C0635330B80E6F199E800D2C0790C6A661600CDAC2A8BB5C037A373480375595516DF8B0EA1EEB200685E4E414A313CF634170B80E6E5879014C3634F73B100685EE701B745879006E616E04BD121D46D1600CDA52A8BF5C0E7A2734803E3E97F9A9B054075702A525A2C8F39CDCD02A03AF861242DCE321E73AA81054073ABCAE22AE0C2E81CD2409C5795C575D121D47D1600D5E5F4E800D2407C383A80FAC102A0BA9C01AC8F0E21F5DC3AE0ECE810EA070B806A5195C58DC0DF47E7907AEE9CAA2C7E121D42FD6001509D5C06909AE5F4BF6A6301509D3E03DC101D42EAA9EB81CF4687507F5800549BAA2CEE00FE3A3A87D453655516774687507F580054379701A46638FDAF5A590054ABAA2CBE065C1A9D43EA99AF5665F18DE810EA170B809AE03715A95E1E53AA9D05404D2801D72AA57ADC017C2C3A84FAC702A0DA5565F103E013D139A49EF88BAA2C3CBB46B5B300A829EF8E0E20F5C06DC09BA343A89F2C006A44551617037F1E9D43EAB8D75665F1FDE810EA270B809AF42BC097A343481DF5FB555914D121D45F160035A62A8B9F014F01FC1093267733F0D2AA2CDE121D44FDB66A7979393A8306204BF99380D7002703DB04C791DAE816E04CE08FABB2B8363A8CFACF02A085CA52FE2846F70C78587416A9453E00BCBE2A8BDBA28368382C005AB82CE5C7021744E7905AE20E60DFAA2CAE8F0EA261710F8016AE2A8B0B81B3A373482DF149077F45B00028CA9F4407905AE2FDD101344C160045B900F849740829D8E555597C2E3A8486C902A0105559DC017C3E3A8714ECFF4407D070590014E9BCE80052B0ABA20368B82C008AE4254E3574374607D070590014E9C7D101A460160085B10028D27F44079082590014C602A04816000DDD4DD101345C160045FA21E0A5283554EBAAB2B8253A8486CB02A0305559AC03D646E790827C333A8086CD02A068FF161D400AF28DE8001A360B80A259003454BEF715CA02A0687E086AA87CEF2B940540D1FC10D450F9DE57280B80A25D0EDC151D425AB09FE2065805B30028D4F84C80ABA373480B767955169E02AB501600B581BBA13534974507902C006A03EF0AA8A1F13DAF701600B5C1B9D101A405F33DAF70AB96975D8652BC2CE5DF07F688CE212DC0E555593C323A84E40C80DAE2B3D101A405F1DBBF5AC102A0B6F0435143E17B5DAD6001505B3803A021B81DF87C7408092C006A89AA2CAEC72BA3A9FF2EA8CAE2D6E810125800D42E4E8DAAEF7C8FAB352C006A93B3A203480DF33DAED6B000A835AAB2381FB8323A87D4900BAAB2B8223A84B48105406D737A7400A9211F8E0E206DCC02A0B6F908DE1D50FDB31E38233A84B4310B805AA52A8B6B817F8ECE21D5ECEFABB2B8313A84B4310B80DAE8AFA203483573694BAD6301501B9D03FC243A8454931B80CF448790EEC902A0D6A9CAE236E0CCE81C524D3E5A95C51DD121A47BB200A8ADDE839B01D57D3F03DE1B1D42DA140B805AA92A8BCB814F44E790E674FA7863ABD43A1600B5D95B81E5E810D28CEE00DE111D42DA1C0B805AAB2A8BAF03FF109D439AD1C7AAB2B82A3A84B4391600B5DD5BA2034833B80B785B7408694B2C006AB5AA2C2E02AAE81CD2943E5E95C537A343485B62015017380BA02E5966B47F456A350B805AAF2A8B2FE28554D41D67546571697408696B2C00EA8AD730BAA18AD466B700BF191D429A8405409D5095C5B781B747E790B6E20FAAB2F87E74086912160075C93B806F47879036E36BC0FBA2434893B200A833AAB2580FFC6A740E691396815755657167741069521600754A5516FF049C1D9D43BA873FAFCAE282E810D2342C00EAA2DFC0DB05AB3DFE03F8EDE810D2B42C00EA9CAA2CAE037E273A8734F6EB5559FCBFE810D2B4562D2F7BAF15755396F24F00CF8ECEA141FB8BAA2C5E161D429A853300EAB29701574787D060FD3BA3EB53489D6401506755657113701AF0B3E82C1A9CDB80E75765715B741069561600755A55161703BF159D4383F39AAA2CFE2D3A84340FF700A817B294FF0D706A740E0DC247ABB278717408695ECE00A82F7E09581B1D42BD7705F0AAE810521D2C00EA85F17E805318DD8C456AC28F81675565716B7410A90E1600F54655165F079E85770D54FD6E034EAECAE28AE820525D2C00EA95AA2C3E0FBC08B82B388AFAE30E463BFEBDD4AF7AC502A0DEA9CAE2E3787EB6EAF38AAA2C3E151D42AA9B0540BD5495C5FB813F8ECEA1CEFBDDAA2CFE323A84D4044F0354AF6529FF33208FCEA14E7A5F5516CE24A9B79C0150DFBD0AF858740875CE5F02BF1E1D426A923300EABD2CE5AB80F702BF169D459DF0AEAA2CBCBAA47ACF02A0C1C852FE26E0ADD139D45ACBC06F5565F1EEE820D2225800342859CA5F0E7C10D8263A8B5AE50EE0E555599C1E1D445A140B8006274BF9298CF605EC109D45ADB08ED179FEFF101D445A240B8006294BF91380BF03768DCEA2503701CFACCAE25FA283488BE659001AA4AA2CCE03FE363A87C25DE4E0AFA1B20068C81E1B1D40E11E1D1D408A6201D0206529DF0338203A87C23D284BF99AE81052040B8086EA71D101D41A47470790225800345416006DE0328006C902A0A1B2006803670034489E06A8C1C952BE0BA3D3BFBC1890006E061E5495C59DD141A44572064043741C0EFE5AB12B2E0368802C001A22A7FF754F4F8E0E202D9A0540436401D03D59003438EE01D0A06429DF96D1FAFFCED159D42AEB81075465B12E3A88B428CE0068688EC0C15FF7B63DF0F8E810D222590034344EFF6B735C06D0A05800343416006D8E054083E21E000DC678FDFF87C003A3B3A89596817DAAB2F85E741069119C01D0909C8883BF366F15F0DCE810D2A258003424CF8B0EA0D67B7E740069515C02D0206429DF06F801B05B7416B5DA32B06F5516D74607919AE60C80866209077F6DDD2A9C29D240580034147EA86B522E0368105C0250EF8DA7FFAF071E129D459DB1A62A8B6BA243484D72064043F0041CFC351D678CD47B16000D811FE69AD669D101A4A6B904A05ECB52BE13701D70FFE82CEA9C23ABB2F85A7408A929CE00A8EF5E8083BF66F3ABD101A4265900D477BF121D409DF5C22CE55E3952BD6501506F65293F0E38323A873A6B47E097A243484DB100A8CF5E1D1D409DF7AA2CE57E4EAA977C63AB97B294EF861774D1FCF6074E8A0E2135C102A0BE7A19B07D7408F582FB48D44B9E06A8DE194FD97E0758131C45FDB00C3CA22A8B6F450791EAE40C80FAE8241CFC559F55B89F443D6401501FFD5E7400F5CE2BB294EF191D42AA930540BD92A5FC54E0D8E81CEA9D9D803F8C0E21D5C93D00EA8DF15DFF2E030E89CEA25EBA0338B42A8B2BA28348757006407DF2121CFCD59C6D81B7458790EAE20C807A214BF90EC095C0DED159D47BC7576571417408695ECE00A82F7E05077F2DC63BA30348757006409D97A57C57E02AC01BB768519E5995C53F448790E6E10C80FAE00D38F86BB1DEEE3D02D475BE81D569E373B35F1B9D43837328F0D2E810D23C2C00EABA3F03768E0EA1417A779672F79DA8B32C00EAAC2CE5AF044E8ECEA1C17A00F0119702D455BE71D54959CA1F01FCCFE81C1ABC27026F8C0E21CDC2B300D43959CAB703CE071E1D9D45627485C013AAB2B8383A88340D6700D4457F8483BFDA635BE0AFB394EF121D449A8605409D92A5FCF138E5AAF63910F8D3E810D2345C0250678C2FF8F37560DFE82CD2669C5695C559D121A4493803A02EF9DF38F8ABDD3E94A57C9FE810D2242C00EA842CE5BF0BA4E81CD256DC1FF85496F207440791B6C62500B55E96F25F063E189D439AC205C093ABB2F869741069732C006AB52CE5CF03CEC0D92A75CFB9C0C95559FC2C3A88B4297EA8AAB5B2943F13F828BE4FD54D4F01CECC52BE7D74106953FC60552B6529CF817380FB446791E6700A70AE7B02D4461600B54E96F23733BAC9CF36C151A43A3C1EF87296F235D141A48DB90740AD91A57C4746A7FAFD627416A9013F044EADCAE2FCE820125800D41259CA1FCD68BDFFE0E82C5283EE04DE01FC515516B74787D1B05900142A4BF9368C2EEDFB6660BBD834D2C27C0D78715516DF880EA2E1B200284C96F2A7016FC11BFB6898D603EF01DE5595C58DD161343C16002D5C96F213813F064E88CE22B5C0CD8C8AC07BABB2B8253A8C86C302A085C852FE50E039C00B81E382E3486DF4634657BC3CB32A8BCBA2C3A8FF2C006A4C96F2BD180DFACF031E0BAC8A4D2475C615C0D9C059960135C502A05A65297F182B83FE0938E84BF3DA5006CEAECAE2D2E830EA0F0B80E696A57C6F5606FDE371D0979A6219506D2C009AC978D07F2EA341FF381CF4A545BB92956502CB80A66601D0C4B294EFC3CAA07F2C0EFA525B6C2803675765F1F5E830EA060B80B6284BF9BEDC7DD097D46E96014DC402A07B19DFB464C3A07F4C6C1A4973F8162BCB049601DD8D0540006429DF8F9541FF31C17124D56F431938BB2A8BAF4587513C0BC0806529DF9F9541FFE8E0389216C732200BC0D064293F809541DF6BF04BFA362BCB04968101B1000C4096F2035919F48F0A8E23A9BD369481B3ABB2F8D7E8306A9605A0A7B2941FC4CAA07F64701C49DD6319E8390B408F64297F382B83FE11C17124F5C777585926B00CF48405A0E3C683FEF3C6FF1C1E1C4752FF6D2803675765F1D5E8309A9D05A083B2943F829541FFBF05C791345C96810EB300744496F2835919F40F0B8E2349F7F41DE0E38C96092C031D600168B12CE587B032E81F1A1C47922675152B33035F890EA34DB300B44C96F247B232E83F2A388E24CDCB32D052168016C852FE285606FD4706C791A4A65CC5CA328165209805204896F2435919F40F098E23498B76352B330397448719220BC00265293F8C9541FFE0E03892D41696810016808665293F9C958BF33C22388E24B5DDD5AC2C1358061A6401684096F223180DF8CF051E1E1C4792BA6A2D2B3303170767E91D0B404DB2941FC9CAA07F50701C49EA9BB558066A6501984396F2A35819F40F0C8E234943B196956502CBC08C2C0053CA52FE685606FD0382E348D2D05DC3CACCC045D161BAC40230812CE547B332E8EF1F1C4792B469968129580036234BF9635819F4F70B8E23499ACE358C9609CEAECAE2C2E8306D6401D84896F2635819F4D7C4A69124D5C432B009832F0059CA8F6565D0DF37388E24A959DF65BC4C005C5495C56007C1411680F179FA2F6634E8EF131C479214E3BB8C66063E5295C5D7A2C32CDA600A4096F21D81D3805702C706C79124B5CB85C0078133ABB258171D66117A5F00B2943F82D1A0FF12E001C1712449ED7623703AF0C1AA2CAE880ED3A45E16802CE5DB01A7321AF87F2E388E24A99B3EC76856E09CAA2C6E8F0E53B75E1580F1C0FF6AE07780DD83E34892FAE187C0DB81F7F7A908F4A60064297F16F02EBC0EBF24A919DF02DE5095C5DF4607A943E70BC0F87AFCEF019E189D459234085F005E5795C557A383CCA3B305204BF95EC0DB189DCEB72A388E24695896818F006FAACAE2BAE830B3E85C01C852BE13F046E0F5C04EC1712449C3761BF06EE09D5559DC161D661A9D2A0059CA1F059C053C323A8B24491BB91C787E5516DF880E32A9D5D1012695A5FC65C0C538F84B92DAE710E0A22CE579749049B57E06204BF92E8CCEC34CD15924499AC019C02BAAB2F84974902D697501C8527E38A329FF8747679124690ADF064E6BF39902AD5D02C852FE4AE0021CFC2549DD7320707E96F2D74407D99CD6CD008CAFE6F757C00B83A3489254878F032FAACA627D74908DB5AA006429DF01F8047052741649926A742E704A9B4E156C4D01186FF6FB3BBC798F24A99FCE034E6ECBE6C05614802CE5F7073E0D1C1F9D4592A4065D043CAD2A8B1BA3838417802CE5BB011570646810499216E3EBC053AAB2F8516488D00290A57C4FE0B378711F49D2B05C0E3CB92A8BEBA3028415802CE5FB00FF0C1C10124092A458DF014EACCAE2BB112F1E5200C61BFEBE0C1CB6F0179724A93D2E034EA8CAE2D645BFF0C22F0494A57C15703A0EFE92241D067C783C362E54C49500FF007876C0EB4A92D446A7321A1B176AA14B0059CA4F01FE065878D39124A9C59681E7546571CEA25E706105204BF9A1C0F9C02E0B79414992BAE556E0F8AA2CBEB188175B4801C852FE40E06260FFC65F4C92A4EEBA0A38A62A8B1F37FD428DEF01C852BE0DA35BFA3AF84B92B465FB03676529DFB6E9175AC426C0D7034F5AC0EB4892D4072702BFD5F48B34BA0490A57C3FE0DF801D1B7B114992FA671D706855165735F5024DCF00BC1F077F4992A6B523A331B4318D15802CE5A7014F6BEAF92549EAB9A78EC7D24634B20490A57C57E09BC01EB53FB92449C3F17DE090AA2C6EAEFB899B9A0178070EFE9224CD6B4FE0AD4D3C71ED330059CA8F07BE8457FB9324A90E7731BA40D045753E69AD05607CDEE257F1463F9224D5E95F81C7546571675D4F58F712C00B71F09724A96E4702B56E08ACAD008C6F65F886BA9E4F9224DD4DAD1707AA7306E0E9C0A1353E9F24495A717896F2ACAE27ABB300347ED942499206AEB6B1B6964D8059CA8F012E9C3F8E24490C3422BB0000046549444154DA8AA3ABB2F8CABC4F52D70C80DFFE25495A8C5AC6DCB96700B2941FC4E8AA7F8BB8B3A02449437727F0F0796F1454C7A0FDFA9A9E4792246DDD36C06FCEFB2473CD0064297F00703DB0C3BC412449D2C4D6017BCE738F8079BFB99F8C83BF24498BB623F08C799E60DE0270CA9CBF2F49926633D7183CF3124096F21D811F013BCF13409224CDE45660B7AA2CD6CFF2CBF3CC003C05077F4992A2EC023C69D65F9EA70038FD2F4952AC99C7E2999600B2946F03FC00D86DD61796244973FB21F0D0AA2CEE9AF617679D01781C0EFE922445DB1D386E965F9CB50038FD2F49523BCC3426CF5A007E7EC6DF932449F57AD62CBF34F51E802CE5FB026B6779314992D488BDABB2F8DE34BF30CB0CC0D20CBF2349929AB334ED2F58002449EABEA5697FC102204952F72D4DFB0B53ED0170FD5F92A4D69A6A1FC0B433004B533E5E92242DC6D2340FB6004892D40F4BD33CD8022049523F2C4DF3E089F7006429DF07B86686409224693126DE0730CD0CC0D26C592449D2822C4DFA400B802449FDB134E9032D009224F5C7D2A40F9C680F80EBFF922475C63E55595CBBB5074D3A03B0345F164992B4204B933CC802204952BF2C4DF2200B802449FDB234C983B6BA07C0F57F49923A67ABFB0026990178624D612449D2622C6DED01931480AD3E8924496A95A5AD3DC002204952FF2C6DED015BDC0390A57C6FE0BB35069224498BB1C57D005B9B0158AA378B24495A90A52DFDA5054092A47E5ADAD25F5A002449EAA7A52DFDE566F700B8FE2F4952E76D761FC09666003CFF5F92A46EFBB9CDFDC5960AC052FD392449D2022D6DEE2F2C009224F5D7D2E6FE62937B00B2943F0CD8EABD84254952EBED5B95C5BDF6F46D6E0660A9D92C922469419636F5430B802449FDB6B4A91F5A002449EAB7A54DFDF05E7B005CFF9724A977EEB50F605333004B8BC92249921664E99E3FD85401F002409224F5CBD23D7FE00C802449FDB774CF1FDC6D0F4096F2BD80EF2D309024495A8CBBED03B8E70CC0D262B34892A40559DAF83F2C0092240DC3DD6E0C6401902469189636FE8FFFDA03E0FABF2449BDB7A62A8B6BE0EE33004B31592449D2822C6DF8C3C605C0F3FF2549EAB7A50D7F70064092A4E158DAF08755CBCBCB64297F28705D5C1E4992B4206BAAB2B866C30CC05264124992B4304BB0B204B014164392242DD212580024491A9A2580554F79E1CB5DFF97246958D6ACC66FFF92240DCDD26AE009D1292449D2423D7135F0D0E814922469A10E590DDC373A8524495AA87D2C0092240DCF7D2D0092240DCFD757039745A79024490B75C96AE0A3D1292449D2425DBC1AF834706374124992B410EB812FAEAECAE267C019D1692449D242FC615516D76EB817C01B81AF46A69124498DBB107837C0AAE5E56500B294EF0E7C19D83F2E9724496AC83AE0A8AA2CBE092B7703A42A8B1F024F05BE1F144C922435E342361AFC61A302005095C5B7818380DFC422204952D7AD077E1B78ECC6833F6CB404704F59CAB7075E063C07D817D81BB84FB3392549D21C7E0AFC2B70097031F0C5AA2CAEDDD403FF3F5DCF60047E74B8BA0000000049454E44AE426082";
        eventImage.setFileData(eventImageString.getBytes());
        return eventImage;
    }

    static List<Event> createUpcomingEventsList() {
        return Arrays.asList(
                createEvent(1L, "Java Dev Talks #1", DATE_TIME.plusWeeks(1L), createParticipantsEventList()),
                createEvent(2L, "Java Dev Talks #2", DATE_TIME.plusWeeks(2L), createParticipantsEventList()),
                createEvent(3L, "Java Dev Talks #3", DATE_TIME.plusWeeks(3L), createParticipantsEventList())
        );
    }

    static List<Event> createPastEventsList() {
        return Arrays.asList(
                createEvent(3L, "Java Dev Talks #3", DATE_TIME.minusWeeks(1L), createParticipantsEventList()),
                createEvent(2L, "Java Dev Talks #2", DATE_TIME.minusWeeks(2L), createParticipantsEventList()),
                createEvent(1L, "Java Dev Talks #1", DATE_TIME.minusWeeks(3L), createParticipantsEventList())
        );
    }

    static List<AppUser> createParticipantsEventList() {
        return Arrays.asList(
                createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole()),
                createAppUser(3L, "Jan", "Nowak", "jannowak@example.com", createUserRole())
        );
    }

    static List<String> createCitiesList() {
        return Arrays.asList(
                "Częstochowa",
                "Kraków",
                "Nowy Sącz",
                "Przemyśl",
                "Wrocław",
                "Żary",
                "Łódź",
                "Ć",
                "Poznań"
        );
    }
}
