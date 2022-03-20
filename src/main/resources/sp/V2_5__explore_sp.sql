IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'explore_findall')
		  )
  DROP PROCEDURE explore_findall
GO

CREATE PROCEDURE explore_findall
          @pageNumber  INT =1,
          @pageSize INT = 10,
		  @searchByTitle nvarchar(250) =null,
		  @merchantId int = null
      AS

       DECLARE @cnt INT
       DECLARE @offset INT

              SELECT COUNT(*) AS cnt FROM [explore] (NOLOCK)
			  WHERE
			   	(merchant_id = @merchantId or @merchantId is null) and
				(title like '%' + @searchByTitle + '%' or @searchByTitle is null)

               SET @pageNumber = ABS(@pageNumber)
               SET @pageSize = ABS(@pageSize)
               IF @pageNumber < 1
                SET @pageNumber = 1
               IF @pageSize < 1
                SET @pageSize = 1

               SET @offset = (@pageNumber - 1) * @pageSize

               SELECT * FROM [explore] (NOLOCK)
			   WHERE
					(merchant_id = @merchantId or @merchantId is null) and
					(title like '%' + @searchByTitle + '%' or @searchByTitle is null)

               ORDER BY id OFFSET @offset ROWS FETCH NEXT @pageSize ROWS ONLY
GO


IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'explore_insertupdate')
		  )
  DROP PROCEDURE explore_insertupdate
GO

CREATE PROCEDURE [dbo].explore_insertupdate
	@Id bigint,
	@Title nvarchar(255) = NULL,
	@description nvarchar(1000),
	@imageUrl nvarchar(255),
	@pageUrl varchar(255),
	@type nvarchar(255),
	@MerchantId int = null,

	@modifiedBy nvarchar(255),
	@createdBy nvarchar(255),
	@dateCreated  datetime = getdate,
	@dateModified datetime = getdate
AS

IF EXISTS(SELECT [Id] FROM [dbo].explore (NOLOCK) WHERE [Id] = @Id and merchant_id = @MerchantId)
BEGIN
	UPDATE [dbo].explore SET
		[title] = @Title,
		[description] = @description,
		[image_url] = @imageUrl,
		[page_url] = @pageUrl,
		[type] = @type,
		[merchant_id] = @MerchantId,

		--Audit Properties --
		modified_by = @modifiedBy,
		date_modified = @dateModified
	WHERE
		([Id] = @Id) and merchant_id = @MerchantId

	 SELECT @Id = app.id FROM [dbo].[explore] app (nolock)
	 WHERE  [Id] = @Id and [merchant_id] = @MerchantId
RETURN @Id
END
ELSE
BEGIN

INSERT INTO [dbo].[explore](
	[title],
	[description],
	[image_url],
	[page_url],
	[type],
	[merchant_id],

	[modified_by],
	[created_by],
	[date_created],
	[date_modified]
) VALUES (
	@Title,
	@description,
	@imageUrl,
	@pageUrl,
	@type,
	@MerchantId,

	@modifiedBy,
	@createdBy,
	@dateCreated,
	@dateModified
)

SET @Id = SCOPE_IDENTITY()
SELECT @Id

RETURN @Id

END
 --- end insert


GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'delete_explore_byid')
		  )
  DROP PROCEDURE delete_explore_byid

GO
CREATE PROCEDURE [dbo].[delete_explore_byid]
	@Id smallint,
	@MerchantId int
AS

DELETE [dbo].[explore] WHERE [Id] = @Id and merchant_id =  @MerchantId



GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'explore_getbyid')
		  )
  DROP PROCEDURE [explore_getbyid]
GO

CREATE PROCEDURE [dbo].[explore_getbyid]
	@Id smallint,
	@merchantId int
AS

SELECT top 1* FROM [dbo].[explore] (nolock)
WHERE
	[Id] = @Id and (merchant_id = @merchantId or @merchantId is null)


GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'explore_getbytitle'))
  DROP PROCEDURE explore_getbytitle
GO

CREATE PROCEDURE [dbo].[explore_getbytitle]
	@title nvarchar(250),
	@merchantId int
AS

SELECT top 1 * FROM [dbo].[explore] (nolock)
WHERE title = @title and (merchant_id = @merchantId or @merchantId is null)