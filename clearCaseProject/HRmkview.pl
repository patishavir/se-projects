# uni_mkview.pl perl script for Unified Make View Command
#
# The script to be run from any BAT file with the following parameters:
# 
# 1. STREAM-tag - Mandatory
# 2. COMPONENTS - Mandatory
# 3. TargetFolder - Optionally in format: targetFolder=PATH
# 4. isDynamicView - Optionally un format: mkDynamicView=Yes (as ENV variable)
#
# --------------------------------------------- 
# Oded, Meir (mic), July 2004, Hermesh Project.
# version 0.1.1 13.07.2004
############################################################

# -------------- Start ------------------------------

$| = 1;   		# Try autoflash
$DEBUG = 0;
$UsageMess = "\n\nThe $0 script makes a new view.\nUsage: ccperl $0 stream [components] [targetFolder=PATH]\n\n"; 

$CT = qw(cleartool.exe);
$defaultTargetFolder = qq(C:\\CCViews);
$UserName = $ENV{"USERNAME"};
$CompName = $ENV{"COMPUTERNAME"};
$addTagSuffix = $ENV{"addTagSuffix"};
$targetFolder = $ENV{"targetFolder"};
$isDynamicView = $ENV{"mkDynamicView"};
$isReadOnlyView = $ENV{"mkReadOnlyView"};
undef $addTagSuffix unless $addTagSuffix eq "yes";
undef $isDynamicView unless $isDynamicView eq "yes";
undef $isReadOnlyView unless $isReadOnlyView eq "yes";
undef $viewKind;
$viewKind = " -snapshot" unless $isDynamicView;
undef $addTagSuffix if $isDynamicView;
undef $targetFolder if $isDynamicView;
$stgLoc = " -stgloc VIEWs12 ";
$stgLoc = " -stgloc VIEWs12 " if $isDynamicView;
my %ThePvobs;
$paramKeyPreffix = qq(targetFolder=);
print $UsageMess if $DEBUG;
print "addTagSuffix=$addTagSuffix, targetFolder=$targetFolder, isDynamicView=$isDynamicView, stgLoc=$stgLoc\n" if $DEBUG;

# ------------- Make arrays of components ----------------------

@AllVobs = `$CT lsvob`; 		# don't forget leading \
chomp(@AllVobs);
my $counter = 0;
foreach $vobname (@AllVobs) {
	next unless $vobname =~ ucmvob;
	print "DBG: splitting $vobname \n" if $DEBUG;
	my $a, $b, $kuku;
	($a,$b,$kuku) = split /\s+/, $vobname;
	$UCMVobs[$counter] = $b;
	$counter++;
}

my $command = $CT . " lscomp -s -invob ";
foreach $aPvob (@UCMVobs) {
	$cmd = $command . $aPvob;
	chomp(@allComponents = `$cmd`);
	foreach $component (@allComponents) {
		$ThePvobs{$component} = $aPvob;
	}
}

# ------------- Check the arguments ----------------------------
die "Too many arguments \n $UsageMess" if $#ARGV <1;		# Die without Stream

# ------------- Stream Name & Components ------------

$StreamTag = shift;
print "DBG: Stream name is $StreamTag\n" if $DEBUG;

@Components = @ARGV;
print "DBG: Components: @Components\n" if $DEBUG;

# ---- Check and Create the View Root directory ---
$targetFolder = $defaultTargetFolder unless $targetFolder;
`mkdir $targetFolder` unless -d $targetFolder;		# Create it if doesn't exsist yet

# ------------- Prepare the make-view command -------

$ViewTag = $UserName . "_" . $StreamTag;
$ViewTag = $StreamTag . "_" . $UserName . "_Dyn" if $isDynamicView;
$tagSuffix = substr($CompName,8);
$ViewTag .= "_" . $tagSuffix if $addTagSuffix;
$ViewRoot = $targetFolder . "\\" . $StreamTag;
print "DBG: View Root is $ViewRoot and Tag is $ViewTag\n" if $DEBUG;

$ENV{"CLEARCASE_CURRENT_VIEW_TAG"} = $ViewTag;

# ------------- Let's take the first Component as Master One --

$comp = $Components[0];
$UCMVob = '@' . $ThePvobs{$comp};
print "DBG: Founded Component $comp in PVob $UCMVob\n" if $DEBUG;

$StreamName = $StreamTag . $UCMVob;
print "DBG: Stream Name is $StreamName\n" if $DEBUG;
$pTime = "";
$pTime = " -ptime " unless $isDynamicView;
$MVcommand = $CT . " mkview" . $viewKind . $pTime . " -tag " . $ViewTag . " -stream " . $StreamName . $stgLoc;
$MVcommand.= $ViewRoot unless $isDynamicView;
print "Make view command: $MVcommand\n";

# ------------ Make View ------------------------------

die "MakeView Command Failed: $! $? \n" unless `$MVcommand`;

if ($isReadOnlyView) {
	$CVCommand = $CT . " chview -readonly "  . $ViewTag;
	print "Change view command: $CVCommand\n";
	`$CVCommand`;

}

print "The View $ViewTag has been created\n" if $DEBUG;;
if ($isDynamicView) {
	my $startViewCommand = $CT . " startview " . $ViewTag;
	print "Start view command: $startViewCommand\n";
	`$startViewCommand`;
	exit 0;
}

# -------- Prepare the update -add_loadrules command ---

$ULcommand = $CT . " update -add_loadrules";	# First part of the command

#-----------------------------------------------------------------											
# Now, if there are several components, add 'em with the path. 
# Otherwise - add the vob tag only.
# 
# Use Component Root Dir instead of Component itself
#------------------------------------------------------------------

$SecondPart = '';
if ($#Components >= 0) {
	# ---------- There are more 1+ components ----
	my $c = $#Components + 1;
	print "DBG: There are $c components\n" if $DEBUG;
	foreach $comp (@Components) {
	#
	# Here we should put Component's Root-Dir 
	#

		my $lscomp = $CT . " lscomp component:" . $comp . $UCMVob;
		my @tmp;
		die "BAD Component $comp\n" unless (@tmp = `$lscomp`);
		$RootDir = @tmp[1];
		
		die "Bad Component $comp\n" unless $RootDir =~ "root directory";
		($tmp[0],$cdr) = split("root directory: ",$RootDir);
		($tmp[0],$CompRootDir,$kuku) = split /"/,$cdr;
		
		print "DBG: Component root direcoty: $CompRootDir\n" if $DEBUG;
		$SecondPart .= " " . $ViewRoot . $CompRootDir;
		print "DBG: Second:\n$SecondPart\n" if $DEBUG;
		}
	}
else {
	# ---------- There are NO components ---------
	$VobTag="";
	$SecondPart = " " . $ViewRoot . "\\" . $VobTag;
}
$ULcommand .= $SecondPart;
print "DBG: Update-add_loadrules command: $ULcommand\n";

# ------------ Update View and Add-Load Rules ----

# $ULcommand = $CT . " lsvob -s" if $DEBUG;		# In order to avoid ;-)

$startString = localtime;
$startTime = time;
#sleep 5;
system($ULcommand);
$finishString = localtime;
$finishTime = time;
my $dd= $finishTime - $startTime;

print "\n\n$0: The View $ViewTag has been updated\n Started  at $startString\n Finished at $finishString\n";
print "Elapsed time $dd secs\n";
# ------------- Finish ------------------------------
undef($ENV{"CLEARCASE_CURRENT_VIEW_TAG"});
exit;
__END__