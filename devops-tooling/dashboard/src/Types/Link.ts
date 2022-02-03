interface ILink {
  id:number,
  source:number,
  target:number,
  issued: string
  status?:string,
}

export default ILink;
