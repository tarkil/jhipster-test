
const enum Rarity {
    'MYTHIC',
    'RARE',
    'UNCOMMON',
    'COMMON',
    'SPECIAL',
    'BASIC'

};
export class Card {
    constructor(
        public id?: number,
        public name?: string,
        public cost?: string,
        public textFlavour?: string,
        public description?: string,
        public attack?: number,
        public defense?: number,
        public trait?: string,
        public rarity?: Rarity,
        public typeId?: number,
        public reprintsId?: number,
    ) { }
}
