// this addon adds potion trades to journyman clerics
// credit goes to Firigion and the renewable_elytra_trade.sc script
// credit also goes to _Taser_Monkey_ who provided the nbt data for each custom potion.


// Radomly Choose Between these Potion types.
global_potion_types = ['splash_potion','lingering_potion','potion'];
global_potion_emerald_cost = [4,8];
// Give each cleric 0-3 new potion trades.
global_max_potion_trades = 3;
// The level a cleric must be inorder to develop potion trades.
// levels go from 1-5 novice though Master
// I do not recommend setting this to 1, 
// It prevent cycled clerics from developing trades
global_potion_master_level = 3;

// Nbt data for each avalible trade.
global_potion_trades = [
	// Dolphins Grace
	'{
		sell: {id: "minecraft:%s", Count:1, tag:{
			CustomPotionEffects:[{Duration:9600, Id: 30, Amplifier: 3}], 
			CustomPotionColor: 5592575, display: {Name:\'{ "text":"Potion of Grace"}\'}}
		},
		buy: {id: "minecraft:emerald", Count: %d},
		buyB: {id: "minecraft:tropical_fish", Count: 1},
		uses: 0,
		demand: -20,
		priceMutiplier: 0.0,
		specialPrice: 0,
		rewardExp: 1,
		xp: 1
	}',
	// Haste
	'{
		sell: {id: "minecraft:%s", Count:1, tag:{
			CustomPotionEffects:[{Duration:9600, Id: 3, Amplifier: 1}], 
			CustomPotionColor: 16755200, display: {Name:\'{ "text":"Potion of Haste"}\'}}
		},
		buy: {id: "minecraft:emerald", Count: %d},
		buyB: {id: "minecraft:raw_gold", Count: 1},
		uses: 0,
		demand: -20,
		priceMutiplier: 0.0,
		specialPrice: 0,
		rewardExp: 1,
		xp: 1
	}',
	// Levitation
	'{
		sell: {id: "minecraft:%s", Count:1, tag:{
			CustomPotionEffects:[{Duration:300, Id: 25, Amplifier: 3}], 
			CustomPotionColor: 16777215, display: {Name:\'{ "text":"Potion of Levitation"}\'}}
		},
		buy: {id: "minecraft:emerald", Count: %d},
		buyB: {id: "minecraft:phantom_membrane", Count: 1},
		uses: 0,
		demand: -20,
		priceMutiplier: 0.0,
		specialPrice: 0,
		rewardExp: 1,
		xp: 1
	}',
	// Wither
	'{
		sell: {id: "minecraft:%s", Count:1, tag:{
			CustomPotionEffects:[{Duration:170, Id: 20, Amplifier: 3}], 
			CustomPotionColor: 0, display: {Name:\'{ "text":"Potion of Wither"}\'}}
		},
		buy: {id: "minecraft:emerald", Count: %d},
		buyB: {id: "minecraft:wither_rose", Count: 1},
		uses: 0,
		demand: -20,
		priceMutiplier: 0.0,
		specialPrice: 0,
		rewardExp: 1,
		xp: 1
	}',
	// Mining Fatigue
	'{
		sell: {id: "minecraft:%s", Count:1, tag:{
			CustomPotionEffects:[{Duration:2800, Id: 4, Amplifier: 0}], 
			CustomPotionColor: 5592405, display: {Name:\'{ "text":"Potion of Mining Fatigue"}\'}}
		},
		buy: {id: "minecraft:emerald", Count: %d},
		buyB: {id: "minecraft:soul_sand", Count: 1},
		uses: 0,
		demand: -20,
		priceMutiplier: 0.0,
		specialPrice: 0,
		rewardExp: 1,
		xp: 1
	}',
	// Blindness
	'{
		sell: {id: "minecraft:%s", Count:1, tag:{
			CustomPotionEffects:[{Duration:330, Id: 15, Amplifier: 0}], 
			CustomPotionColor: 70000, display: {Name:\'{ "text":"Potion of Blindness"}\'}}
		},
		buy: {id: "minecraft:emerald", Count: %d},
		buyB: {id: "minecraft:ender_eye", Count: 1},
		uses: 0,
		demand: -20,
		priceMutiplier: 0.0,
		specialPrice: 0,
		rewardExp: 1,
		xp: 1
	}'
	
];



_add_trades(cleric, p) -> (
	// add 0-3 potion trades
	loop(rand(1 + global_max_potion_trades),

		trade = nbt('{Offers: Recipes:['+str(
			// select a random trade from the list above
			global_potion_trades:rand(length(global_potion_trades)),
			// Randomize the potion type 
			global_potion_types:rand(length(global_potion_types)),
			// randomize the cost of each trade
			(global_potion_emerald_cost:0) + rand(global_potion_emerald_cost:1)
		)+']}');
		
		modify(cleric, 'nbt_merge', trade);
		// run(str('/data modify entity %s Offers.Recipes append value %s', cleric~'command_name', trade));
	);

	// Dont add any more potions to a potion master
	modify(cleric, 'tag', 'IsPotionMaster'); 

);

__on_player_interacts_with_entity(player, entity, hand) -> (
	if(entity~'type'=='villager',
		nbt = entity~'nbt';
		// If Villager is a cleric and is a journyman and not already a potion master
		if(nbt:'VillagerData':'profession'=='minecraft:cleric' && 
			nbt:'VillagerData':'level' >= global_potion_master_level &&
			parse_nbt(nbt:'Tags')~'IsPotionMaster' == null
			, 
			_add_trades(entity, player)
		);
	)
);


